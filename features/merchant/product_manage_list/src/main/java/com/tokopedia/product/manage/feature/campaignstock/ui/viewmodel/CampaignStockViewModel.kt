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
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStatusUseCase
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
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
import com.tokopedia.shop.common.data.model.ProductStock
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
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
        private val editStatusUseCase: EditStatusUseCase,
        private val editStockUseCase: UpdateProductStockWarehouseUseCase,
        private val editProductVariantUseCase: EditProductVariantUseCase,
        private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
        private val getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.main) {

    private var isStockVariant: Boolean = false
    private var campaignProductName: String = ""
    private var campaignReservedStock: Int = 0

    private var shopId: String = ""
    private var productId: String = ""
    private var warehouseId: String = ""

    private var nonVariantStock: Int = 0
    private var nonVariantReservedStock = 0
    private var nonVariantStatus = ProductStatus.INACTIVE

    private var currentNonVariantStock: Int = 0
    private var currentNonVariantStatus = ProductStatus.INACTIVE

    private var variantList: List<ProductVariant> = emptyList()
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
                        val warehouseId = getWarehouseId(shopId)
                        campaignStockAllocationUseCase.params = CampaignStockAllocationUseCase.createRequestParam(productIds, shopId, warehouseId)
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
        nonVariantStatus = if(isActive) {
            ProductStatus.ACTIVE
        } else {
            ProductStatus.INACTIVE
        }
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
                var result: Success<UpdateCampaignStockResult> = Success(UpdateCampaignStockResult(
                    productId,
                    campaignProductName,
                    currentNonVariantStock + nonVariantReservedStock,
                    currentNonVariantStatus,
                    true
                ))

                if(nonVariantStatus != currentNonVariantStatus) {
                    result = editProductStatus(nonVariantStatus)
                }

                if(nonVariantStock != currentNonVariantStock) {
                    result = editProductStock(nonVariantStock, result.data.isSuccess)
                }

                mProductUpdateResponseLiveData.value = result
            },
            onError = {
                mProductUpdateResponseLiveData.value = Fail(it)
            }
        )
    }

    private suspend fun editProductStock(nonVariantStock: Int, isUpdateStatusSuccess: Boolean = true): Success<UpdateCampaignStockResult> {
        return withContext(dispatchers.io) {
            val requestParams = UpdateProductStockWarehouseUseCase.createRequestParams(
                shopId,
                productId,
                getWarehouseId(shopId),
                nonVariantStock.toString()
            )
            val response = editStockUseCase.execute(requestParams)
            // If edit product status success, use current status. If fail, use initial status
            val successProductStatus =
                    if (isUpdateStatusSuccess) {
                        nonVariantStatus
                    } else {
                        currentNonVariantStatus
                    }
            val status = response.getProductStatus() ?: successProductStatus

            Success(UpdateCampaignStockResult(
                productId,
                campaignProductName,
                nonVariantStock + nonVariantReservedStock,
                status,
                true
            ))
        }
    }

    private suspend fun editProductStatus(status: ProductStatus): Success<UpdateCampaignStockResult> {
        return withContext(dispatchers.io) {
            editStatusUseCase.setParams(shopId, productId, status)
            val response = editStatusUseCase.executeOnBackground()

            val productUpdateV3Data = response.productUpdateV3Data
            val errorMessage = productUpdateV3Data.header.errorMessage
            val isSuccess = productUpdateV3Data.isSuccess

            val message = when {
                errorMessage.isNotEmpty() -> errorMessage.last()
                !isSuccess -> com.tokopedia.product.manage.common.R.string
                        .product_stock_reminder_toaster_failed_desc.toString()
                else -> null
            }

            Success(UpdateCampaignStockResult(
                productId,
                campaignProductName,
                nonVariantStock + nonVariantReservedStock,
                status,
                isSuccess,
                message
            ))
        }
    }

    private fun updateVariantData() {
        launchCatchError(
            block = {
                var result: Result<UpdateCampaignStockResult> =  with(editVariantResult) {
                    val status = getVariantStatus()
                    val totalStock = countVariantStock() + campaignReservedStock

                    Success(UpdateCampaignStockResult(
                        productId,
                        productName,
                        totalStock,
                        status,
                        true
                    ))
                }

                if(shouldEditVariantStatus()) {
                    result = editVariantStatus()
                }

                if(shouldEditVariantStock()) {
                    result = editVariantStock()
                }

                mProductUpdateResponseLiveData.value = result
            },
            onError = {
                mProductUpdateResponseLiveData.value = Fail(it)
            }
        )
    }

    private suspend fun editVariantStatus(): Success<UpdateCampaignStockResult> {
        return withContext(dispatchers.io) {
            val updateVariantParam = ProductManageVariantMapper.mapResultToUpdateParam(shopId, editVariantResult)
            val editProductVariantParam = EditProductVariantUseCase.createRequestParams(updateVariantParam)
            val editStockResponse = editProductVariantUseCase.execute(editProductVariantParam)

            with(editVariantResult) {
                val status = getVariantStatus()
                val totalStock = countVariantStock() + campaignReservedStock

                Success(UpdateCampaignStockResult(
                    productId,
                    productName,
                    totalStock,
                    status,
                    editStockResponse.productUpdateV3Data.isSuccess,
                    editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
                ))
            }
        }
    }

    private suspend fun editVariantStock(): Success<UpdateCampaignStockResult> {
        return withContext(dispatchers.io) {
            with(editVariantResult) {
                val status = getVariantStatus()
                val totalStock = countVariantStock() + campaignReservedStock
                val productList = variants.map { ProductStock(it.id, it.stock.toString()) }
                val requestParams = UpdateProductStockWarehouseUseCase.createRequestParams(
                    shopId,
                    getWarehouseId(shopId),
                    productList
                )
                editStockUseCase.execute(requestParams)

                Success(UpdateCampaignStockResult(
                    productId,
                    campaignProductName,
                    totalStock,
                    status,
                    true
                ))
            }
        }
    }

    private suspend fun getNonVariantResult(
        productId: String,
        stockAllocationData: GetStockAllocationData
    ): NonVariantStockAllocationResult {
        val warehouseId = getWarehouseId(userSession.shopId)
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productId, warehouseId)

        val otherCampaignStockData = otherCampaignStockDataUseCase.executeOnBackground()
        nonVariantStock = stockAllocationData.summary.sellableStock.toIntOrZero()
        nonVariantStatus = otherCampaignStockData.status

        currentNonVariantStock = nonVariantStock
        currentNonVariantStatus = nonVariantStatus

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

        val warehouseId = getWarehouseId(userSession.shopId)
        val getProductVariantUseCaseRequestParams = GetProductVariantUseCase.createRequestParams(productId, warehouseId = warehouseId)
        otherCampaignStockDataUseCase.params = OtherCampaignStockDataUseCase.createRequestParams(productId, warehouseId)

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
            val sellableProductList = stockAllocationData.detail.sellable
            val variants = it.variants.map { variant ->
                val stock = sellableProductList.firstOrNull { product ->
                    product.productId == variant.id
                }?.stock.toIntOrZero()
                variant.copy(stock = stock)
            }
            val getVariantResult = it.copy(variants = variants)
            val variantsEditResult = ProductManageVariantMapper.mapVariantsToEditResult(productId, getVariantResult)
            editVariantResult = variantsEditResult
            variantList = variants
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

    private suspend fun getWarehouseId(shopId: String): String {
        return if (warehouseId.isEmpty()) {
            val response = getAdminInfoShopLocationUseCase.execute(shopId.toIntOrZero())
            warehouseId = response.firstOrNull { it.isMainLocation() }?.locationId.toString()
            warehouseId
        } else {
            warehouseId
        }
    }

    private fun shouldEditVariantStatus(): Boolean {
        variantList.forEachIndexed { index, variant ->
            val inputStatus = editVariantResult.variants[index].status
            val currentStatus = variant.status
            if (inputStatus != currentStatus) {
                return true
            }
        }
        return false
    }

    private fun shouldEditVariantStock(): Boolean {
        variantList.forEachIndexed { index, variant ->
            val inputStock = editVariantResult.variants[index].stock
            val currentStock = variant.stock
            if (inputStock != currentStock) {
                return true
            }
        }
        return false
    }
}