package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper.mapProductManageOwnerAccess
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.UpdateCampaignStockResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.VariantStockAllocationResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CampaignStockViewModel @Inject constructor(
        private val campaignStockAllocationUseCase: CampaignStockAllocationUseCase,
        private val otherCampaignStockDataUseCase: OtherCampaignStockDataUseCase,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val editStockUseCase: EditStockUseCase,
        private val editProductVariantUseCase: EditProductVariantUseCase,
        private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private var isStockVariant: Boolean = false
    private var campaignProductName: String = ""
    private var campaignReservedStock: Int = 0

    private var shopId: String = ""
    private var productId: String = ""

    private var nonVariantStock: Int = 0
    private var nonVariantReservedStock = 0
    private var nonVariantIsActive = false

    private var editVariantResult: EditVariantResult = EditVariantResult()
    private val mProductManageAccess = MutableLiveData<ProductManageAccess>()
    private val mShowSaveBtn = MutableLiveData<Boolean>()

    private val mProductUpdateResponseLiveData = MutableLiveData<Result<UpdateCampaignStockResult>>()
    private val mGetStockAllocationLiveData = MutableLiveData<Result<StockAllocationResult>>()

    val getStockAllocationData: LiveData<Result<StockAllocationResult>>
        get() = mGetStockAllocationLiveData

    val productUpdateResponseLiveData: LiveData<Result<UpdateCampaignStockResult>>
        get() = mProductUpdateResponseLiveData

    val showSaveBtn: LiveData<Boolean>
        get() = mShowSaveBtn

    fun getStockAllocation(productIds: List<String>) {
        if(productIds.isNotEmpty()) {
            productId = productIds.first()

            launchCatchError(
                block = {
                    mGetStockAllocationLiveData.value = Success(withContext(dispatchers.io) {
                        campaignStockAllocationUseCase.params = CampaignStockAllocationUseCase.createRequestParam(productIds, shopId)
                        val stockAllocationData = campaignStockAllocationUseCase.executeOnBackground()
                        campaignProductName = stockAllocationData.summary.productName
                        stockAllocationData.summary.isVariant.let { isVariant ->
                            isStockVariant = isVariant

                            if (isVariant) {
                                getVariantResult(productId, stockAllocationData)
                            } else {
                                getNonVariantResult(productId, stockAllocationData)
                            }
                        }
                    })
                },
                onError = {
                    mGetStockAllocationLiveData.value = Fail(it)
                }
            )
        }
    }

    fun setShopId(shopId: String) {
        this.shopId = shopId
    }

    fun updateNonVariantStockCount(stock: Int) {
        nonVariantStock = stock
    }

    fun updateNonVariantReservedStockCount(reservedStockCount: Int) {
        nonVariantReservedStock = reservedStockCount
    }

    fun updateNonVariantIsActive(isActive: Boolean) {
        nonVariantIsActive = isActive
    }

    fun updateVariantStockCount(productId: String, stockCount: Int) {
        val variants = editVariantResult.variants

        variants.firstOrNull { it.id == productId }?.let {
            val variantList = variants.toMutableList()
            val index = variants.indexOf(it)
            val variant = it.copy(stock = stockCount)

            editVariantResult = editVariantResult.copy(variants = variantList.apply {
                set(index, variant)
            })
        }
    }

    fun updateVariantIsActive(productId: String, status: ProductStatus) {
        val variants = editVariantResult.variants

        variants.firstOrNull { it.id == productId }?.let {
            val variantList = variants.toMutableList()
            val index = variants.indexOf(it)
            val variant = it.copy(status = status)

            editVariantResult = editVariantResult.copy(variants = variantList.apply {
                set(index, variant)
            })
        }
    }

    fun updateStockData() {
        if (isStockVariant) {
            updateVariantData()
        } else {
            updateNonVariantData()
        }
    }

    private fun updateNonVariantData() {
        launchCatchError(
            block = {
                mProductUpdateResponseLiveData.value = Success(withContext(dispatchers.io) {
                    val status = if (nonVariantIsActive) {
                        ProductStatus.ACTIVE
                    } else {
                        ProductStatus.INACTIVE
                    }
                    editStockUseCase.setParams(shopId, productId, nonVariantStock, status)
                    val editStockResponse = editStockUseCase.executeOnBackground()
                    UpdateCampaignStockResult(
                        productId,
                        campaignProductName,
                        nonVariantStock + nonVariantReservedStock,
                        status,
                        editStockResponse.productUpdateV3Data.isSuccess,
                        editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
                    )
                })
            },
            onError = {
                mProductUpdateResponseLiveData.value = Fail(it)
            }
        )
    }

    private fun updateVariantData() {
        launchCatchError(
            block = {
                mProductUpdateResponseLiveData.value = Success(withContext(dispatchers.io) {
                    val variants = editVariantResult.variants
                    val updateVariantParam = ProductManageVariantMapper.mapResultToUpdateParam(shopId, editVariantResult)
                    val editProductVariantParam = EditProductVariantUseCase.createRequestParams(updateVariantParam)
                    val editStockResponse = editProductVariantUseCase.execute(editProductVariantParam)

                    with(editVariantResult) {
                        val totalStock = variants.sumBy { it.stock } + campaignReservedStock
                        val status = if (variants.any { it.status == ProductStatus.ACTIVE }) {
                            ProductStatus.ACTIVE
                        } else {
                            ProductStatus.INACTIVE
                        }
                        UpdateCampaignStockResult(
                            productId,
                            productName,
                            totalStock,
                            status,
                            editStockResponse.productUpdateV3Data.isSuccess,
                            editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
                        )
                    }
                })
            },
            onError = {
                mProductUpdateResponseLiveData.value = Fail(it)
            }
        )
    }

    private suspend fun getNonVariantResult(
        productId: String,
        stockAllocationData: GetStockAllocationData
    ): NonVariantStockAllocationResult {
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productId)

        val otherCampaignStockData = otherCampaignStockDataUseCase.executeOnBackground()

        nonVariantStock = otherCampaignStockData.stock
        nonVariantIsActive = otherCampaignStockData.getIsActive()

        val productManageAccess = async {
            if(userSession.isShopOwner) {
                mapProductManageOwnerAccess()
            } else {
                getProductManageAccess()
            }
        }

        mProductManageAccess.postValue(productManageAccess.await())

        return NonVariantStockAllocationResult(
                stockAllocationData,
                otherCampaignStockData,
                productManageAccess.await()
        )
    }

    private suspend fun getVariantResult(
        productId: String,
        stockAllocationData: GetStockAllocationData
    ): VariantStockAllocationResult {
        campaignReservedStock = stockAllocationData.summary.reserveStock.toIntOrZero()

        val getProductVariantUseCaseRequestParams = GetProductVariantUseCase.createRequestParams(productId)
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productId)

        val getProductVariantData = async { getProductVariantUseCase.execute(getProductVariantUseCaseRequestParams) }
        val otherCampaignStockData = async { otherCampaignStockDataUseCase.executeOnBackground() }

        val productManageAccess = async {
            if(userSession.isShopOwner) {
                mapProductManageOwnerAccess()
            } else {
                getProductManageAccess()
            }
        }

        val getVariantResult = ProductManageVariantMapper.mapToVariantsResult(
            getProductVariantData.await().getProductV3,
            productManageAccess.await()
        ).also {
            val variantsEditResult = ProductManageVariantMapper.mapVariantsToEditResult(productId, it)
            editVariantResult = variantsEditResult
        }

        mProductManageAccess.postValue(productManageAccess.await())

        return VariantStockAllocationResult(
                getVariantResult,
                stockAllocationData,
                otherCampaignStockData.await(),
                productManageAccess.await()
            )
    }

    fun toggleSaveButton(mainStockTab: Boolean) {
        val canManageStock = mProductManageAccess.value?.editStock == true
        val canManageProduct = mProductManageAccess.value?.editProduct == true
        val shouldShowSaveBtn = (canManageStock || canManageProduct) && mainStockTab
        mShowSaveBtn.value = shouldShowSaveBtn
    }

    private suspend fun getProductManageAccess(): ProductManageAccess {
        val response = getProductManageAccessUseCase.execute(userSession.shopId)
        return ProductManageAccessMapper.mapToProductManageAccess(response)
    }
}