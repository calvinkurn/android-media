package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.campaignstock.domain.model.*
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.param.UpdateVariantParam
import com.tokopedia.product.manage.feature.quickedit.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        private val campaignStockAllocationUseCase: CampaignStockAllocationUseCase,
        private val otherCampaignStockDataUseCase: OtherCampaignStockDataUseCase,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val editStockUseCase: EditStockUseCase,
        private val editProductVariantUseCase: EditProductVariantUseCase,
        dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private val mIsStockVariant = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val mProductUpdateResponseLiveData = MutableLiveData<Result<Boolean>>()
    val productUpdateResponseLiveData: LiveData<Result<Boolean>>
        get() = mProductUpdateResponseLiveData

    private val mProductIdsLiveData = MutableLiveData<List<String>>()
    private val mShopIdLiveData = MutableLiveData<String>()

    private val mNonVariantStockLiveData = MutableLiveData<Int>()
    private val mNonVariantIsActiveLiveData = MutableLiveData<Boolean>()

    private val mEditVariantCampaignParamLiveData = MutableLiveData<List<EditVariantCampaignProductParam>>()
    private val mEditVariantResultLiveData = MutableLiveData<EditVariantResult>()

    private val mGetStockAllocationLiveData = MediatorLiveData<Result<StockAllocationResult>>().apply {
        addSource(mProductIdsLiveData) { productIds ->
            val shopId: String = mShopIdLiveData.value.orEmpty()
            launchCatchError(
                    block = {
                        value = Success(withContext(Dispatchers.IO) {
                            campaignStockAllocationUseCase.params = CampaignStockAllocationUseCase.createRequestParam(productIds, shopId)
                            val stockAllocationData = campaignStockAllocationUseCase.executeOnBackground()
                            stockAllocationData.summary.isVariant.let { isVariant ->
                                mIsStockVariant.postValue(isVariant)
                                if (isVariant) {
                                    getVariantResult(productIds, stockAllocationData)
                                } else {
                                    getNonVariantResult(productIds, stockAllocationData)
                                }
                            }
                        })
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val getStockAllocationData: LiveData<Result<StockAllocationResult>>
        get() = mGetStockAllocationLiveData

    fun getStockAllocation(productIds: List<String>) {
        mProductIdsLiveData.value = productIds
    }

    fun setShopId(shopId: String) {
        mShopIdLiveData.value = shopId
    }

    fun updateNonVariantStockCount(stock: Int) {
        mNonVariantStockLiveData.value = stock
    }

    fun updateNonVariantIsActive(isActive: Boolean) {
        mNonVariantIsActiveLiveData.value = isActive
    }

    fun updateVariantStockCount(productId: String, stockCount: Int) {
        mEditVariantCampaignParamLiveData.run {
            val updatedVariantIndex = value?.indexOfFirst {
                it.productId == productId
            }
            updatedVariantIndex?.let { index ->
                value?.get(index)?.copy(stock = stockCount)?.let { variant ->
                    value = value?.toMutableList()?.apply {
                        set(index, variant)
                    }?.toList()
                }
            }
        }
    }

    fun updateVariantIsActive(productId: String, status: ProductStatus) {
        mEditVariantCampaignParamLiveData.run {
            val updatedVariantIndex = value?.indexOfFirst {
                it.productId == productId
            }
            updatedVariantIndex?.let { index ->
                value?.get(index)?.copy(status = status)?.let { variant ->
                    value = value?.toMutableList()?.apply {
                        set(index, variant)
                    }?.toList()
                }
            }
        }
    }

    fun updateStockData() {
        mIsStockVariant.value?.let { isVariant ->
            if (isVariant) {
                updateVariantData()
            } else {
                updateNonVariantData()
            }
        }
    }

    private fun updateNonVariantData() {
        mProductIdsLiveData.value?.let { productIds ->
            mShopIdLiveData.value?.let { shopId ->
                mNonVariantStockLiveData.value?.let { stock ->
                    mNonVariantIsActiveLiveData.value?.let { isActive ->
                        launchCatchError(
                                block = {
                                    mProductUpdateResponseLiveData.value = Success(withContext(Dispatchers.IO) {
                                        val isActiveParam =
                                                if (isActive) {
                                                    ProductStatus.ACTIVE
                                                } else {
                                                    ProductStatus.INACTIVE
                                                }
                                        editStockUseCase.setParams(shopId, productIds.firstOrNull().orEmpty(), stock, isActiveParam)
                                        val editStockResponse = editStockUseCase.executeOnBackground()
                                        editStockResponse.productUpdateV3Data.isSuccess
                                    })
                                },
                                onError = {
                                    mProductUpdateResponseLiveData.value = Fail(it)
                                }
                        )
                    }
                }
            }
        }
    }

    private fun updateVariantData() {
        mEditVariantResultLiveData.value?.let { editVariantResult ->
            mEditVariantCampaignParamLiveData.value?.let { editVariantCampaignParam ->
                mShopIdLiveData.value?.let { shopId ->
                    launchCatchError(
                            block = {
                                mProductUpdateResponseLiveData.value = Success(withContext(Dispatchers.IO) {
                                    val updateVariantParam = getUpdateVariantParam(editVariantResult, editVariantCampaignParam, shopId)
                                    val editProductVariantParam = EditProductVariantUseCase.createRequestParams(updateVariantParam)
                                    val editStockResponse = editProductVariantUseCase.execute(editProductVariantParam)
                                    editStockResponse.productUpdateV3Data.isSuccess
                                })
                            },
                            onError = {
                                mProductUpdateResponseLiveData.value = Fail(it)
                            }
                    )
                }
            }
        }
    }

    private suspend fun getNonVariantResult(productIds: List<String>,
                                            stockAllocationData: GetStockAllocationData): NonVariantStockAllocationResult {
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productIds.firstOrNull().orEmpty())

        val otherCampaignStockData = otherCampaignStockDataUseCase.executeOnBackground()

        mNonVariantStockLiveData.postValue(otherCampaignStockData.stock)
        mNonVariantIsActiveLiveData.postValue(otherCampaignStockData.isActive)

        return NonVariantStockAllocationResult(
                stockAllocationData,
                otherCampaignStockData
        )
    }

    private suspend fun getVariantResult(productIds: List<String>,
                                         stockAllocationData: GetStockAllocationData): VariantStockAllocationResult {
        val getProductVariantUseCaseRequestParams = GetProductVariantUseCase.createRequestParams(productIds.firstOrNull().orEmpty())
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productIds.firstOrNull().orEmpty())

        val getProductVariantData = async { getProductVariantUseCase.execute(getProductVariantUseCaseRequestParams) }
        val otherCampaignStockData = async { otherCampaignStockDataUseCase.executeOnBackground() }
        val getVariantResult = ProductManageVariantMapper.mapToVariantsResult(getProductVariantData.await().getProductV3).also {
            mEditVariantCampaignParamLiveData.postValue(
                    it.variants.map { variant ->
                        EditVariantCampaignProductParam(variant.id, variant.status, variant.stock)
                    }
            )
            mEditVariantResultLiveData.postValue(
                    ProductManageVariantMapper.mapVariantsToEditResult(productIds.firstOrNull().orEmpty(), it)
            )
        }

        return VariantStockAllocationResult(
                getVariantResult,
                stockAllocationData,
                otherCampaignStockData.await())
    }

    private fun getUpdateVariantParam(editVariantResult: EditVariantResult,
                                      editVariantCampaignParam: List<EditVariantCampaignProductParam>,
                                      shopId: String): UpdateVariantParam {
        val updatedEditVariantResult =
                try {
                    editVariantResult.copy(
                            variants = editVariantCampaignParam.map { param ->
                                editVariantResult.variants.find { it.id == param.productId }?.copy(
                                        status = param.status,
                                        stock = param.stock
                                ) ?: editVariantResult.variants.first()
                            }
                    )
                }
                catch (ex: NoSuchElementException) {
                    editVariantResult
                }
        return ProductManageVariantMapper.mapResultToUpdateParam(shopId, updatedEditVariantResult)
    }
}