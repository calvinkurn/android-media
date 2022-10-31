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
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.CampaignStockAllocationUseCase
import com.tokopedia.product.manage.feature.campaignstock.domain.usecase.OtherCampaignStockDataUseCase
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.*
import com.tokopedia.product.manage.feature.list.view.datasource.TickerStaticDataProvider
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStockMapper
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.model.ProductStock
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
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
    private val maxStockThresholdUseCase: GetMaxStockThresholdUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val tickerStaticDataProvider: TickerStaticDataProvider,
) : BaseViewModel(dispatchers.main) {

    private var isStockVariant: Boolean = false
    private var campaignProductName: String = ""
    private var campaignReservedStock: Int = 0

    var shopId: String = ""
        private set
    var productId: String = ""
        private set
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

    private val mProductUpdateResponseLiveData =
        MutableLiveData<Result<UpdateCampaignStockResult>>()
    private val mGetStockAllocationLiveData = MutableLiveData<Result<StockAllocationResult>>()
    private val _tickerData = MutableLiveData<List<TickerData>>()

    val getStockAllocationData: LiveData<Result<StockAllocationResult>>
        get() = mGetStockAllocationLiveData

    val productUpdateResponseLiveData: LiveData<Result<UpdateCampaignStockResult>>
        get() = mProductUpdateResponseLiveData

    val showSaveBtn: LiveData<Boolean>
        get() = mShowSaveBtn

    val tickerData: LiveData<List<TickerData>>
        get() = _tickerData

    fun getStockAllocation(
        productIds: List<String>,
        isProductBundling: Boolean
    ) {
        if (productIds.isNotEmpty()) {
            productId = productIds.first()

            launchCatchError(
                block = {
                    mGetStockAllocationLiveData.value = Success(withContext(dispatchers.io) {
                        val warehouseId = getWarehouseId(shopId)
                        val stockAllocationDataDeffered = async {
                            campaignStockAllocationUseCase.execute(productIds, shopId, warehouseId, isProductBundling)
                        }

                        val maxStockDeferred = async {
                            try {
                                maxStockThresholdUseCase.execute(shopId)
                            } catch (ex: Exception) {
                                null
                            }
                        }
                        val (stockAllocationData, maxStock) =
                            stockAllocationDataDeffered.await() to maxStockDeferred.await()
                        campaignProductName = stockAllocationData.summary.productName
                        stockAllocationData.summary.isVariant.let { isVariant ->
                            isStockVariant = isVariant
                            if (isVariant) {
                                getVariantResult(
                                    productId,
                                    stockAllocationData,
                                    isProductBundling,
                                    maxStock?.getMaxStockFromResponse()
                                )
                            } else {
                                getNonVariantResult(
                                    productId,
                                    stockAllocationData,
                                    isProductBundling,
                                    maxStock?.getMaxStockFromResponse()
                                )
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
        nonVariantStatus = if (isActive) {
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
                var result: Success<UpdateCampaignStockResult> = Success(
                    UpdateCampaignStockResult(
                        productId,
                        campaignProductName,
                        currentNonVariantStock + nonVariantReservedStock,
                        currentNonVariantStatus,
                        isStockChanged = false,
                        isStatusChanged = false,
                        true
                    )
                )

                var isUpdateStatus = false

                if (nonVariantStatus != currentNonVariantStatus) {
                    result = editProductStatus(nonVariantStatus)
                    isUpdateStatus = true
                }

                if (nonVariantStock != currentNonVariantStock) {
                    result =
                        editProductStock(nonVariantStock, result.data.isSuccess, isUpdateStatus)
                }

                mProductUpdateResponseLiveData.value = result
            },
            onError = {
                mProductUpdateResponseLiveData.value = Fail(it)
            }
        )
    }

    private suspend fun editProductStock(
        nonVariantStock: Int,
        isUpdateStatusSuccess: Boolean,
        isAlsoUpdateStatus: Boolean,
    ): Success<UpdateCampaignStockResult> {
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

            Success(
                UpdateCampaignStockResult(
                    productId,
                    campaignProductName,
                    nonVariantStock + nonVariantReservedStock,
                    status,
                    isStockChanged = true,
                    isStatusChanged = isAlsoUpdateStatus,
                    true
                )
            )
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

            Success(
                UpdateCampaignStockResult(
                    productId,
                    campaignProductName,
                    nonVariantStock + nonVariantReservedStock,
                    status,
                    isStockChanged = false,
                    isStatusChanged = true,
                    isSuccess,
                    message
                )
            )
        }
    }

    private fun updateVariantData() {
        launchCatchError(
            block = {
                var result: Result<UpdateCampaignStockResult> = with(editVariantResult) {
                    val status = getVariantStatus()
                    val totalStock = countVariantStock() + campaignReservedStock

                    Success(
                        UpdateCampaignStockResult(
                            productId,
                            productName,
                            totalStock,
                            status,
                            isStockChanged = false,
                            isStatusChanged = false,
                            true,
                            variantsMap = getMappedVariantsResult()
                        )
                    )
                }

                var isStatusChanged = false

                if (shouldEditVariantStatus()) {
                    result = editVariantStatus()
                    isStatusChanged = true
                }

                if (shouldEditVariantStock()) {
                    result = editVariantStock(isStatusChanged)
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
            val updateVariantParam =
                ProductManageVariantMapper.mapResultToUpdateParam(shopId, editVariantResult)
            val editProductVariantParam =
                EditProductVariantUseCase.createRequestParams(updateVariantParam)
            val editStockResponse = editProductVariantUseCase.execute(editProductVariantParam)

            with(editVariantResult) {
                val status = getVariantStatus()
                val totalStock = countVariantStock() + campaignReservedStock

                Success(
                    UpdateCampaignStockResult(
                        productId,
                        productName,
                        totalStock,
                        status,
                        isStockChanged = false,
                        isStatusChanged = true,
                        editStockResponse.productUpdateV3Data.isSuccess,
                        editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
                        variantsMap = getMappedVariantsResult()
                    )
                )
            }
        }
    }

    private suspend fun editVariantStock(isStatusChanged: Boolean): Success<UpdateCampaignStockResult> {
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

                Success(
                    UpdateCampaignStockResult(
                        productId,
                        campaignProductName,
                        totalStock,
                        status,
                        isStockChanged = true,
                        isStatusChanged = isStatusChanged,
                        true,
                        variantsMap = getMappedVariantsResult()
                    )
                )
            }
        }
    }

    private suspend fun getNonVariantResult(
        productId: String,
        stockAllocationData: GetStockAllocationData,
        isProductBundling: Boolean,
        maxStock: Int?
    ): NonVariantStockAllocationResult {
        val warehouseId = getWarehouseId(userSession.shopId)
        otherCampaignStockDataUseCase.params =
            OtherCampaignStockDataUseCase.createRequestParams(
                productId,
                warehouseId,
                isProductBundling
            )

        val otherCampaignStockData = otherCampaignStockDataUseCase.executeOnBackground()
        nonVariantStock = stockAllocationData.summary.sellableStock.toIntOrZero()
        nonVariantStatus = otherCampaignStockData.status

        currentNonVariantStock = nonVariantStock
        currentNonVariantStatus = nonVariantStatus

        val productManageAccess = async {
            if (userSession.isShopOwner) {
                mapProductManageOwnerAccess()
            } else {
                getProductManageAccess()
            }
        }

        mProductManageAccess.postValue(productManageAccess.await())

        val nonVariantReservedEventInfoUiModels = stockAllocationData.detail.reserve.map {
            CampaignStockMapper.mapToParcellableReserved(it)
        } as ArrayList

        val access = productManageAccess.await()

        val sellableProducts = CampaignStockMapper.getSellableProduct(
            id = productId,
            isActive = otherCampaignStockData.getIsActive(),
            access = access,
            isCampaign = otherCampaignStockData.campaign?.isActive == true,
            maxStock = maxStock,
            sellableList = stockAllocationData.detail.sellable
        ) as ArrayList

        return NonVariantStockAllocationResult(
            maxStock,
            nonVariantReservedEventInfoUiModels,
            stockAllocationData.summary,
            sellableProducts,
            otherCampaignStockData,
            access
        )
    }

    private suspend fun getVariantResult(
        productId: String,
        stockAllocationData: GetStockAllocationData,
        isProductBundling: Boolean,
        maxStock: Int?
    ): VariantStockAllocationResult {
        campaignReservedStock = stockAllocationData.summary.reserveStock.toIntOrZero()

        val variantReservedEventInfoUiModels =
            CampaignStockMapper.mapToVariantReserved(stockAllocationData.detail.reserve) as ArrayList

        val warehouseId = getWarehouseId(userSession.shopId)
        val getProductVariantUseCaseRequestParams =
            GetProductVariantUseCase.createRequestParams(
                productId,
                warehouseId = warehouseId,
                isBundling = isProductBundling
            )
        otherCampaignStockDataUseCase.params =
            OtherCampaignStockDataUseCase.createRequestParams(
                productId,
                warehouseId,
                isProductBundling
            )

        val getProductVariantData =
            async { getProductVariantUseCase.execute(getProductVariantUseCaseRequestParams) }
        val otherCampaignStockData = async { otherCampaignStockDataUseCase.executeOnBackground() }

        val productManageAccess = async {
            if (userSession.isShopOwner) {
                mapProductManageOwnerAccess()
            } else {
                getProductManageAccess()
            }
        }

        val getVariantResult = ProductManageVariantMapper.mapToVariantsResult(
            getProductVariantData.await().getProductV3,
            productManageAccess.await(),
            maxStock
        ).also {
            val sellableProductList = stockAllocationData.detail.sellable
            val variants = it.variants.map { variant ->
                val stock = sellableProductList.firstOrNull { product ->
                    product.productId == variant.id
                }?.stock.toIntOrZero()
                variant.copy(stock = stock)
            }
            val variantResult = it.copy(variants = variants)
            val variantsEditResult =
                ProductManageVariantMapper.mapVariantsToEditResult(productId, variantResult)
            editVariantResult = variantsEditResult
            variantList = variants
        }
        val sellableStockProductUiModels =
            CampaignStockMapper.mapToParcellableSellableProduct(
                stockAllocationData.detail.sellable,
                getVariantResult.variants
            )

        mProductManageAccess.postValue(productManageAccess.await())

        return VariantStockAllocationResult(
            getVariantResult,
            variantReservedEventInfoUiModels,
            stockAllocationData.summary,
            sellableStockProductUiModels,
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

    fun getTickerData() {
        val isMultiLocationShop = userSession.isMultiLocationShop
        _tickerData.value = tickerStaticDataProvider.getTickers(isMultiLocationShop)
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

    private fun EditVariantResult.getMappedVariantsResult(): HashMap<String, UpdateCampaignVariantResult> {
        val variantResultMap = variants.associateBy(
            { it.id },
            { UpdateCampaignVariantResult(it.status, it.stock, it.name) }
        )
        return HashMap(variantResultMap)
    }
}
