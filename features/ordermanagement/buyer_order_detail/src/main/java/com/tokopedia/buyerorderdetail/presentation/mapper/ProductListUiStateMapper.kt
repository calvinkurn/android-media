package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.AddonSummary
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.order_management_common.domain.data.ProductBenefit
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.order_management_common.R as order_management_commonR

object ProductListUiStateMapper {

    private const val MAX_PRODUCT_WHEN_COLLAPSED = 1
    private const val MAX_UNFULFILLED_PRODUCT_WHEN_COLLAPSED = 0
    private const val ORDER_LEVEL_KEY = "ORDER_LEVEL_KEY"

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ProductListUiState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        val p1DataRequestState = getBuyerOrderDetailDataRequestState.getP1DataRequestState
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }

            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(
                    getBuyerOrderDetailRequestState.throwable,
                    p1DataRequestState,
                    currentState
                )
            }

            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState.result,
                    p1DataRequestState,
                    currentState,
                    singleAtcRequestStates,
                    collapseProductList,
                    warrantyClaimButtonImpressed,
                    addOnsExpandableState,
                    bmgmProductBenefitExpandableState
                )
            }
        }
    }

    // This mapper only being used while do atc in bmgm
    // so no need to taking into account about expandable addOns
    fun mapToProductListProductUiModel(
        uiModel: ProductBmgmSectionUiModel.ProductUiModel
    ): ProductListUiModel.ProductUiModel {
        val addOnSummaryUiModel = uiModel.addOnSummaryUiModel
        val addOnsIdentifier = generateAddOnsIdentifier(uiModel.productId, uiModel.orderDetailId)
        val buttonUiModel = uiModel.button
        val actionButton = ActionButtonsUiModel.ActionButton(
            key = buttonUiModel?.key.orEmpty(),
            label = buttonUiModel?.label.orEmpty(),
            popUp = ActionButtonsUiModel.ActionButton.PopUp(
                body = buttonUiModel?.popUp?.body.orEmpty(),
                title = buttonUiModel?.popUp?.title.orEmpty(),
                actionButton = buttonUiModel?.popUp?.actionButton?.map {
                    ActionButtonsUiModel.ActionButton.PopUp.PopUpButton(
                        key = it.key,
                        displayName = it.displayName,
                        color = it.color,
                        type = it.type,
                        uriType = it.uriType,
                        uri = it.uri
                    )
                }.orEmpty()
            ),
            type = buttonUiModel?.type.orEmpty(),
            variant = buttonUiModel?.variant.orEmpty(),
            url = buttonUiModel?.url.orEmpty()
        )

        return ProductListUiModel.ProductUiModel(
            orderId = uiModel.orderId,
            orderStatusId = uiModel.orderStatusId,
            categoryId = uiModel.categoryId,
            category = uiModel.category,
            orderDetailId = uiModel.orderDetailId,
            productId = uiModel.productId,
            productName = uiModel.productName,
            productThumbnailUrl = uiModel.thumbnailUrl,
            priceText = uiModel.productPriceText,
            price = uiModel.price,
            totalPrice = uiModel.totalPrice.toString(),
            totalPriceText = uiModel.totalPriceText,
            quantity = uiModel.quantity,
            productNote = uiModel.productNote,
            addonsListUiModel = AddonsListUiModel(
                addOnIdentifier = addOnsIdentifier,
                totalPriceText = addOnSummaryUiModel?.totalPriceText,
                addonsLogoUrl = addOnSummaryUiModel?.addonsLogoUrl.orEmpty(),
                addonsTitle = addOnSummaryUiModel?.addonsTitle.orEmpty(),
                addonsItemList = addOnSummaryUiModel?.addonItemList?.map {
                    AddonsListUiModel.AddonItemUiModel(
                        priceText = it.priceText,
                        quantity = it.quantity,
                        addonsId = it.addonsId,
                        addOnsName = it.addOnsName,
                        type = it.type,
                        addOnsThumbnailUrl = it.addOnsThumbnailUrl,
                        toStr = it.toStr,
                        fromStr = it.fromStr,
                        message = it.message,
                        providedByShopItself = it.providedByShopItself,
                        infoLink = it.infoLink,
                        tips = it.tips
                    )
                }.orEmpty(),
                canExpandCollapse = true,
                showTotalPrice = true
            ),
            addOnSummaryUiModel = addOnSummaryUiModel,
            isProcessing = uiModel.isProcessing.orFalse(),
            button = actionButton,
            productUrl = uiModel.thumbnailUrl
        )
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: ProductListUiState
    ): ProductListUiState {
        return if (currentState is ProductListUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?,
        p1DataRequestState: GetP1DataRequestState,
        currentState: ProductListUiState
    ): ProductListUiState {
        return when (p1DataRequestState.getInsuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(currentState)
            }

            is GetInsuranceDetailRequestState.Complete -> {
                mapOnGetInsuranceDetailComplete(throwable)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        p1DataRequestState: GetP1DataRequestState,
        currentState: ProductListUiState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        return when (
            val insuranceDetailRequestState =
                p1DataRequestState.getInsuranceDetailRequestState
        ) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(
                    buyerOrderDetailData,
                    insuranceDetailRequestState,
                    currentState,
                    singleAtcRequestStates,
                    collapseProductList,
                    warrantyClaimButtonImpressed,
                    addOnsExpandableState,
                    bmgmProductBenefitExpandableState
                )
            }

            is GetInsuranceDetailRequestState.Complete -> {
                mapOnGetInsuranceDetailComplete(
                    buyerOrderDetailData,
                    insuranceDetailRequestState,
                    singleAtcRequestStates,
                    collapseProductList,
                    warrantyClaimButtonImpressed,
                    addOnsExpandableState,
                    bmgmProductBenefitExpandableState
                )
            }
        }
    }

    private fun mapOnGetInsuranceDetailRequesting(
        currentState: ProductListUiState
    ): ProductListUiState {
        return if (currentState is ProductListUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetInsuranceDetailRequesting(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Requesting,
        currentState: ProductListUiState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        return if (currentState is ProductListUiState.HasData) {
            mapOnReloading(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                currentState,
                singleAtcRequestStates,
                collapseProductList,
                warrantyClaimButtonImpressed,
                addOnsExpandableState,
                bmgmProductBenefitExpandableState
            )
        } else {
            mapOnDataReady(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                singleAtcRequestStates,
                collapseProductList,
                warrantyClaimButtonImpressed,
                addOnsExpandableState,
                bmgmProductBenefitExpandableState
            )
        }
    }

    private fun mapOnGetInsuranceDetailComplete(
        throwable: Throwable?
    ): ProductListUiState {
        return mapOnError(throwable)
    }

    private fun mapOnGetInsuranceDetailComplete(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        return mapOnDataReady(
            buyerOrderDetailData,
            insuranceDetailRequestState,
            singleAtcRequestStates,
            collapseProductList,
            warrantyClaimButtonImpressed,
            addOnsExpandableState,
            bmgmProductBenefitExpandableState
        )
    }

    private fun mapOnLoading(): ProductListUiState {
        return ProductListUiState.Loading
    }

    private fun mapOnReloading(
        currentState: ProductListUiState.HasData
    ): ProductListUiState {
        return ProductListUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnReloading(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        currentState: ProductListUiState.HasData,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        val insuranceDetailData = when (insuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapCurrentStateToProtectionProduct(currentState)
            }

            is GetInsuranceDetailRequestState.Complete.Error -> {
                null
            }

            is GetInsuranceDetailRequestState.Complete.Success -> {
                insuranceDetailRequestState.result?.protectionProduct
            }
        }
        return ProductListUiState.HasData.Reloading(
            mapProductListUiModel(
                buyerOrderDetailData.details,
                buyerOrderDetailData.details?.bundleIcon.orEmpty(),
                buyerOrderDetailData.shop,
                buyerOrderDetailData.addonInfo,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                insuranceDetailData,
                singleAtcRequestStates,
                collapseProductList,
                warrantyClaimButtonImpressed,
                addOnsExpandableState,
                bmgmProductBenefitExpandableState
            )
        )
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiState {
        val insuranceDetailData = insuranceDetailRequestState.let {
            if (it is GetInsuranceDetailRequestState.Complete.Success) {
                it.result?.protectionProduct
            } else {
                null
            }
        }
        return ProductListUiState.HasData.Showing(
            mapProductListUiModel(
                buyerOrderDetailData.details,
                buyerOrderDetailData.details?.bundleIcon.orEmpty(),
                buyerOrderDetailData.shop,
                buyerOrderDetailData.addonInfo,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                insuranceDetailData,
                singleAtcRequestStates,
                collapseProductList,
                warrantyClaimButtonImpressed,
                addOnsExpandableState,
                bmgmProductBenefitExpandableState
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): ProductListUiState {
        return ProductListUiState.Error(throwable)
    }

    private fun mapCurrentStateToProtectionProduct(
        currentState: ProductListUiState.HasData
    ): GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct {
        return GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct(
            protections = mapCurrentStateToProtections(currentState)
        )
    }

    private fun mapCurrentStateToProtections(
        currentState: ProductListUiState.HasData
    ): List<GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection> {
        return arrayListOf<GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection>().apply {
            appendNonBundleProductsProtection(currentState.data.productList)
            appendBundleProductsProtection(currentState.data.productBundlingList)
        }
    }

    private fun ArrayList<GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection>.appendNonBundleProductsProtection(
        productList: List<ProductListUiModel.ProductUiModel>
    ) {
        productList.forEach { product ->
            product.insurance?.let { productInsurance ->
                add(mapProductInsuranceToProtection(product.productId, "", productInsurance))
            }
        }
    }

    private fun ArrayList<GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection>.appendBundleProductsProtection(
        productBundlingList: List<ProductListUiModel.ProductBundlingUiModel>
    ) {
        productBundlingList.forEach { productBundle ->
            productBundle.bundleItemList.forEach { product ->
                product.insurance?.let { productInsurance ->
                    add(
                        mapProductInsuranceToProtection(
                            product.productId,
                            productBundle.bundleId,
                            productInsurance
                        )
                    )
                }
            }
        }
    }

    private fun mapProductInsuranceToProtection(
        productId: String,
        bundleId: String,
        productInsurance: ProductListUiModel.ProductUiModel.Insurance
    ): GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection {
        return GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection(
            bundleID = bundleId,
            isBundle = bundleId.toLongOrZero().isMoreThanZero(),
            productID = productId,
            protectionConfig = GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection.ProtectionConfig(
                icon = GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection.ProtectionConfig.Icon(
                    productInsurance.logoUrl
                ),
                wording = GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection.ProtectionConfig.Wording(
                    id = GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct.Protection.ProtectionConfig.Wording.Id(
                        label = productInsurance.label
                    )
                )
            )
        )
    }

    /**
     * Map the response into various UI models which will be placed on the product list view section.
     * You should know that this section have expand-collapse functionality. Therefore if you're
     * going to add a new type of UI models to be placed on the product list view section. You should
     * handle the mapping based on whether the product list is collapsed or expanded based on the
     * collapseProductList value.
     */
    private fun mapProductListUiModel(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details?,
        bundleIcon: String,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): ProductListUiModel {
        /**
         * Map product bmgm response into UI model and limit the number of mapped items based on
         * MAX_PRODUCT_WHEN_COLLAPSED. The numOfRemovedProductBmgm is indicating the number of
         * unmapped product bmgm which will be used on the toggle view to show remaining hidden product
         * when the product list view is collapsed. The productBmgmList contains the UI models
         * which limited by the MAX_PRODUCT_WHEN_COLLAPSED.
         */
        val (numOfRemovedProductBmgm, productBmgmList) = mapProductBmgm(
            details?.bmgms,
            details?.bmgmIcon.orEmpty(),
            orderId,
            orderStatusId,
            details?.addonLabel.orEmpty(),
            details?.addonIcon.orEmpty(),
            collapseProductList,
            MAX_PRODUCT_WHEN_COLLAPSED,
            singleAtcResultFlow,
            insuranceDetailData,
            warrantyClaimButtonImpressed,
            addOnsExpandableState,
            bmgmProductBenefitExpandableState
        )

        /**
         * Map product bundle response into UI model and limit the number of mapped items based on
         * MAX_PRODUCT_WHEN_COLLAPSED. The numOfRemovedProductBundle is indicating the number of
         * unmapped product bundle which will be used on the toggle view to show remaining hidden product
         * when the product list view is collapsed. The productBundlingList contains the UI models
         * which limited by the MAX_PRODUCT_WHEN_COLLAPSED.
         */
        val (numOfRemovedProductBundle, productBundlingList) = mapProductBundle(
            details?.bundles,
            bundleIcon,
            addonInfo?.label.orEmpty(),
            addonInfo?.iconUrl.orEmpty(),
            addOnsExpandableState,
            orderId,
            orderStatusId,
            insuranceDetailData,
            singleAtcResultFlow,
            collapseProductList,
            MAX_PRODUCT_WHEN_COLLAPSED - productBmgmList.size,
            warrantyClaimButtonImpressed
        )

        /**
         * Map non-bundled product response into UI model and limit the number of items based on
         * MAX_PRODUCT_WHEN_COLLAPSED minus the number of mapped product bundle.
         * The numOfRemovedNonProductBundle is indicating the number of unmapped non-bundled product
         * which will be used on the toggle view to show remaining hidden product when the product list
         * view is collapsed. The nonProductBundlingList contains the non-bundled product UI models which
         * limited by the MAX_PRODUCT_WHEN_COLLAPSED minus the number of mapped product bundle.
         */
        val (numOfRemovedNonProductBundle, nonProductBundlingList) = details?.let {
            val mapProductList = mapProductList(
                details = it,
                orderId = orderId,
                orderStatusId = orderStatusId,
                insuranceDetailData = insuranceDetailData,
                singleAtcResultFlow = singleAtcResultFlow,
                collapseProductList = collapseProductList,
                remainingSlot = MAX_PRODUCT_WHEN_COLLAPSED - productBmgmList.size - productBundlingList.size,
                isPof = false,
                shop = shop,
                warrantyClaimButtonImpressed = warrantyClaimButtonImpressed,
                addOnsExpandableState = addOnsExpandableState
            )
            mapProductList
        } ?: (Int.ZERO to emptyList())

        /**
         * Map order-level addons response into UI model and limit the number of items based on
         * MAX_PRODUCT_WHEN_COLLAPSED minus the number of mapped bundled and non-bundled product.
         * The numOfRemovedAddOn is indicating the number of unmapped order-level addons which will
         * be used on the toggle view to show remaining hidden product when the product list view is
         * collapsed. The addOnList contains the order-level addons UI models which
         * limited by the MAX_PRODUCT_WHEN_COLLAPSED minus the number of mapped bundled and non-bundled product.
         */
        val (numOfRemovedAddOn, addOnList) = getAddonsSectionOrderLevel(
            addonInfo = addonInfo,
            collapseProductList = collapseProductList,
            remainingSlot = MAX_PRODUCT_WHEN_COLLAPSED - productBmgmList.size - productBundlingList.size - nonProductBundlingList.size
        )
        val (numOfRemovedUnfulfilled, unFulfilledProductList) = details?.partialFulfillment?.unfulfilled?.details?.let {
            getUnFulfilledProducts(
                details = details,
                orderId = orderId,
                orderStatusId = orderStatusId,
                insuranceDetailData = insuranceDetailData,
                singleAtcResultFlow = singleAtcResultFlow,
                collapseProductList = collapseProductList,
                remainingSlot = MAX_UNFULFILLED_PRODUCT_WHEN_COLLAPSED,
                isPof = true,
                warrantyClaimButtonImpressed = warrantyClaimButtonImpressed,
                addOnsExpandableState = addOnsExpandableState
            )
        } ?: (Int.ZERO to emptyList())
        val tickerDetails = mapTickerDetails(details?.tickerInfo)
        return ProductListUiModel(
            productList = nonProductBundlingList,
            productBmgmList = productBmgmList,
            productUnFulfilledList = unFulfilledProductList,
            productListHeaderUiModel = mapProductListHeaderUiModel(shop, orderId, orderStatusId),
            productBundlingList = productBundlingList,
            productUnfulfilledHeaderLabel = mapPofUnfulfilledHeaderLabelUiModel(details?.partialFulfillment),
            productFulfilledHeaderLabel = mapPofFulfilledHeaderLabelUiModel(details?.partialFulfillment),
            addonsListUiModel = addOnList,
            productListToggleUiModel = mapProductListToggleUiModel(
                collapseProductList = collapseProductList,
                numOfRemovedProductBmgm = numOfRemovedProductBmgm,
                numOfRemovedProductBundle = numOfRemovedProductBundle,
                numOfRemovedNonProductBundle = numOfRemovedNonProductBundle,
                numOfRemovedAddOn = numOfRemovedAddOn,
                numOfRemovedUnFulfilledProduct = numOfRemovedUnfulfilled
            ),
            tickerInfo = tickerDetails
        )
    }

    private fun mapProductListToggleUiModel(
        collapseProductList: Boolean,
        numOfRemovedProductBmgm: Int,
        numOfRemovedProductBundle: Int,
        numOfRemovedNonProductBundle: Int,
        numOfRemovedAddOn: Int,
        numOfRemovedUnFulfilledProduct: Int
    ): ProductListUiModel.ProductListToggleUiModel? {
        return if (collapseProductList) {
            val numOfRemovedItems =
                numOfRemovedProductBmgm + numOfRemovedProductBundle + numOfRemovedNonProductBundle + numOfRemovedAddOn + numOfRemovedUnFulfilledProduct
            if (numOfRemovedItems.isMoreThanZero()) {
                ProductListUiModel.ProductListToggleUiModel(
                    collapsed = true,
                    text = StringRes(
                        R.string.buyer_order_detail_product_list_expand,
                        listOf(numOfRemovedItems)
                    )
                )
            } else {
                null
            }
        } else {
            ProductListUiModel.ProductListToggleUiModel(
                collapsed = false,
                text = StringRes(R.string.buyer_order_detail_product_list_collapse)
            )
        }
    }

    private fun mapProductList(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        orderId: String,
        orderStatusId: String,
        isPof: Boolean,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        remainingSlot: Int,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop? = null,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>
    ): Pair<Int, List<ProductListUiModel.ProductUiModel>> {
        /**
         * Reduce the non-bundle response items to be mapped based on the remaining slot on the product
         * list view when collapsed (Ex: if there is 5 non-bundle on the response and the product list view
         * can only contains 1 more non-bundle, then only map 1 non-bundle response).
         * The numOfRemovedNonBundles indicate the number of unmapped non-bundle response.
         * The reducedNonBundles contains the non-bundle response which will be mapped into UI model.
         */
        val (numOfRemovedNonBundles, reducedNonBundles) = details.nonBundles?.run {
            if (collapseProductList) {
                (size - remainingSlot).coerceAtLeast(Int.ZERO) to take(remainingSlot)
            } else {
                Int.ZERO to this
            }
        } ?: (Int.ZERO to null)
        val mappedNonBundles = reducedNonBundles?.map {
            mapProduct(
                details = details,
                product = it,
                addonSummary = it.addonSummary,
                orderId = orderId,
                orderStatusId = orderStatusId,
                isPof = isPof,
                insuranceDetailData = insuranceDetailData,
                singleAtcResultFlow = singleAtcResultFlow,
                shop = shop,
                warrantyClaimButtonImpressed = warrantyClaimButtonImpressed,
                addOnsExpandableState = addOnsExpandableState
            )
        }.orEmpty()
        return numOfRemovedNonBundles to mappedNonBundles
    }

    @Suppress("SameParameterValue")
    private fun mapProductBundle(
        bundleDetail: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle>?,
        bundleIcon: String,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        remainingSlot: Int,
        warrantyClaimButtonImpressed: Boolean
    ): Pair<Int, List<ProductListUiModel.ProductBundlingUiModel>> {
        /**
         * Reduce the bundle response items to be mapped based on the remaining slot on the product
         * list view when collapsed (Ex: if there is 5 product bundle on the response and the product list view
         * can only contains 1 more product bundle, then only map 1 product bundle response).
         * The numOfRemovedBundleDetail indicate the number of unmapped product bundle response.
         * The reducedBundleDetail contains the product bundle response which will be mapped into UI model.
         */
        val (numOfRemovedBundleDetail, reducedBundleDetail) = bundleDetail?.run {
            if (collapseProductList) {
                (size - remainingSlot).coerceAtLeast(Int.ZERO) to take(remainingSlot)
            } else {
                Int.ZERO to this
            }
        } ?: (Int.ZERO to null)
        val mappedProductBundle = reducedBundleDetail?.map { bundle ->
            ProductListUiModel.ProductBundlingUiModel(
                bundleId = bundle.bundleId,
                bundleName = bundle.bundleName,
                bundleIconUrl = bundleIcon,
                totalPrice = bundle.bundleSubtotalPrice,
                totalPriceText = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                bundleItemList = bundle.orderDetail.map { bundleDetail ->
                    mapProductBundleItem(
                        bundleDetail,
                        orderId,
                        orderStatusId,
                        bundle.bundleId,
                        addOnLabel,
                        addOnIcon,
                        addOnsExpandableState,
                        insuranceDetailData,
                        singleAtcResultFlow,
                        warrantyClaimButtonImpressed
                    )
                }
            )
        }.orEmpty()
        return numOfRemovedBundleDetail to mappedProductBundle
    }

    private fun mapProductBmgm(
        bundleDetail: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bmgm>?,
        bmgmIcon: String,
        orderId: String,
        orderStatusId: String,
        addOnLabel: String,
        addOnIcon: String,
        collapseProductList: Boolean,
        remainingSlot: Int,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: List<String>
    ): Pair<Int, List<ProductBmgmSectionUiModel>> {
        /**
         * Reduce the bmgm response items to be mapped based on the remaining slot on the product
         * list view when collapsed (Ex: if there is 5 product bmgm on the response and the product list view
         * can only contains 1 more product bmgm, then only map 1 product bmgm response).
         * The numOfRemovedBmgmDetail indicate the number of unmapped product bundle response.
         * The reducedBmgmDetail contains the product bundle response which will be mapped into UI model.
         */
        val (numOfRemovedBmgmDetail, reducedBmgmDetail) = bundleDetail?.run {
            if (collapseProductList) {
                (size - remainingSlot).coerceAtLeast(Int.ZERO) to take(remainingSlot)
            } else {
                Int.ZERO to this
            }
        } ?: (Int.ZERO to null)
        val mappedProductBmgm = reducedBmgmDetail?.map { bmgm ->
            ProductBmgmSectionUiModel(
                bmgmId = bmgm.id,
                bmgmName = bmgm.bmgmTierName,
                bmgmIconUrl = bmgmIcon,
                totalPrice = bmgm.priceBeforeBenefit,
                totalPriceText = bmgm.priceBeforeBenefitFormatted,
                totalPriceReductionInfoText = bmgm.totalPriceNote,
                bmgmItemList = bmgm.orderDetail.map { orderDetail ->
                    mapProductBmgmItem(
                        orderDetail,
                        orderId,
                        orderStatusId,
                        addOnLabel,
                        addOnIcon,
                        singleAtcResultFlow,
                        insuranceDetailData,
                        warrantyClaimButtonImpressed,
                        addOnsExpandableState
                    )
                },
                productBenefits = mapBmgmProductBenefit(
                    bmgm.productBenefit,
                    orderId,
                    bmgm.id,
                    !bmgmProductBenefitExpandableState.contains(bmgm.id)
                )
            )
        }.orEmpty()
        return numOfRemovedBmgmDetail to mappedProductBmgm
    }

    private fun mapBmgmProductBenefit(
        productBenefit: ProductBenefit?,
        orderId: String,
        bmgmId: String,
        expanded: Boolean
    ): AddOnSummaryUiModel? {
        return productBenefit?.let {
            if (productBenefit.isValid()) {
                AddOnSummaryUiModel(
                    addOnIdentifier = bmgmId,
                    totalPriceText = StringRes(order_management_commonR.string.om_gwp_collapsed_title_format, listOf(productBenefit.orderDetail?.count().orZero())),
                    addonsLogoUrl = productBenefit.iconUrl,
                    addonsTitle = productBenefit.label,
                    addonItemList = mapBmgmProductBenefitItems(productBenefit.orderDetail, orderId),
                    canExpandCollapse = true
                ).apply { isExpand = expanded }
            } else null
        }
    }

    private fun mapBmgmProductBenefitItems(
        orderDetails: List<ProductBenefit.OrderDetail>?,
        orderId: String
    ): List<AddOnSummaryUiModel.AddonItemUiModel> {
        return orderDetails?.map { orderDetail ->
            AddOnSummaryUiModel.AddonItemUiModel(
                priceText = orderDetail.totalPriceText,
                quantity = orderDetail.quantity,
                addonsId = orderDetail.orderDetailId.toString(),
                addOnsName = orderDetail.productName,
                type = String.EMPTY,
                addOnsThumbnailUrl = orderDetail.thumbnail,
                toStr = String.EMPTY,
                fromStr = String.EMPTY,
                message = String.EMPTY,
                descriptionExpanded = false,
                noteCopyable = false,
                providedByShopItself = true,
                infoLink = String.EMPTY,
                tips = String.EMPTY,
                orderId = orderId,
                orderDetailId = orderDetail.orderDetailId.toString()
            )
        }.orEmpty()
    }

    private fun mapProductListHeaderUiModel(
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        orderId: String,
        orderStatusId: String
    ): ProductListUiModel.ProductListHeaderUiModel {
        return ProductListUiModel.ProductListHeaderUiModel(
            orderId = orderId,
            shopBadgeUrl = shop.badgeUrl,
            shopName = shop.shopName,
            shopType = shop.shopType,
            shopId = shop.shopId,
            orderStatusId = orderStatusId
        )
    }

    private fun mapPofFulfilledHeaderLabelUiModel(
        partialFulfillment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.PartialFulfillment?
    ): ProductListUiModel.ProductPofHeaderLabelUiModel? {
        return partialFulfillment?.let {
            ProductListUiModel.ProductPofHeaderLabelUiModel(
                title = it.fulfilled.header.title,
                quantity = it.fulfilled.header.quantity,
                isUnfulfilled = false
            )
        }
    }

    private fun mapTickerDetails(tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo?): TickerUiModel? {
        return tickerInfo?.let {
            TickerUiModel(
                actionKey = tickerInfo.actionKey,
                actionText = tickerInfo.actionText,
                actionUrl = tickerInfo.actionUrl,
                description = tickerInfo.text,
                type = tickerInfo.type
            )
        }
    }

    private fun mapPofUnfulfilledHeaderLabelUiModel(
        partialFulfillment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.PartialFulfillment?
    ): ProductListUiModel.ProductPofHeaderLabelUiModel? {
        return partialFulfillment?.let {
            ProductListUiModel.ProductPofHeaderLabelUiModel(
                title = it.unfulfilled.header.title,
                quantity = it.unfulfilled.header.quantity,
                isUnfulfilled = true
            )
        }
    }

    private fun getAddonsSectionOrderLevel(
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?,
        collapseProductList: Boolean,
        remainingSlot: Int
    ): Pair<Int, AddonsListUiModel?> {
        //Order level doesnt really have unique identifier, assign with normal string will be okay
        //Because there is only one add ons card in one order.
        val addOnsIdentifier = ORDER_LEVEL_KEY
        val mappedAddOn = addonInfo?.let { addonInfo ->
            AddonsListUiModel(
                addOnIdentifier = addOnsIdentifier,
                addonsTitle = addonInfo.label,
                addonsLogoUrl = addonInfo.iconUrl,
                totalPriceText = StringRes(order_management_commonR.string.raw_string_format, listOf(addonInfo.orderLevel?.totalPriceStr.orEmpty())),
                addonsItemList = addonInfo.orderLevel?.addons?.map {
                    val addonNote = it.metadata?.addonNote
                    val infoLink = it.metadata?.infoLink
                    AddonsListUiModel.AddonItemUiModel(
                        priceText = it.priceStr,
                        addOnsName = it.name,
                        type = it.type,
                        addonsId = it.id,
                        quantity = it.quantity,
                        addOnsThumbnailUrl = it.imageUrl,
                        toStr = addonNote?.to.orEmpty(),
                        fromStr = addonNote?.from.orEmpty(),
                        message = addonNote?.notes.orEmpty(),
                        providedByShopItself = false,
                        infoLink = infoLink.orEmpty(),
                        tips = addonNote?.tips.orEmpty()
                    )
                }.orEmpty(),
                canExpandCollapse = false,
                showTotalPrice = false
            )
        }

        return if (mappedAddOn?.addonsItemList.isNullOrEmpty()) {
            Int.ZERO to null
        } else {
            mappedAddOn?.run {
                if (collapseProductList) {
                    if (remainingSlot.isZero()) {
                        Int.ONE to null
                    } else {
                        (Int.ONE - remainingSlot).coerceAtLeast(Int.ZERO) to this
                    }
                } else {
                    Int.ZERO to this
                }
            } ?: (Int.ZERO to null)
        }
    }

    private fun getUnFulfilledProducts(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        orderId: String,
        orderStatusId: String,
        isPof: Boolean,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        remainingSlot: Int,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>
    ): Pair<Int, List<ProductListUiModel.ProductUiModel>> {
        val (numOfRemovedUnfulfilled, reducedUnfulfilled) = details.partialFulfillment?.unfulfilled?.details?.run {
            if (collapseProductList) {
                (size - remainingSlot).coerceAtLeast(Int.ZERO) to take(remainingSlot)
            } else {
                Int.ZERO to this
            }
        } ?: (Int.ZERO to null)
        val mappedUnfulfilled = reducedUnfulfilled?.map {
            mapProduct(
                details = details,
                product = it,
                addonSummary = it.addonSummary,
                orderId = orderId,
                orderStatusId = orderStatusId,
                isPof = isPof,
                insuranceDetailData = insuranceDetailData,
                singleAtcResultFlow = singleAtcResultFlow,
                warrantyClaimButtonImpressed = warrantyClaimButtonImpressed,
                addOnsExpandableState = addOnsExpandableState
            )
        }.orEmpty()
        return numOfRemovedUnfulfilled to mappedUnfulfilled
    }

    private fun mapProduct(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle,
        addonSummary: AddonSummary?,
        orderId: String,
        orderStatusId: String,
        isPof: Boolean,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop? = null,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>
    ): ProductListUiModel.ProductUiModel {
        return ProductListUiModel.ProductUiModel(
            button = mapActionButton(product.button),
            category = product.category,
            categoryId = product.categoryId,
            orderDetailId = product.orderDetailId,
            orderId = orderId,
            orderStatusId = orderStatusId,
            price = product.price,
            priceText = product.priceText,
            productId = product.productId,
            productName = product.productName,
            productNote = product.notes,
            productThumbnailUrl = product.thumbnail,
            quantity = product.quantity,
            totalPrice = product.totalPrice,
            totalPriceText = product.totalPriceText,
            isProcessing = singleAtcResultFlow[product.productId] is AddToCartSingleRequestState.Requesting,
            addonsListUiModel = getAddonsSectionProductLevel(
                product.productId,
                product.orderDetailId,
                details,
                addonSummary,
                addOnsExpandableState
            ),
            insurance = mapInsurance(product.productId, insuranceDetailData),
            isPof = isPof,
            productUrl = product.productUrl,
            shopId = shop?.shopId,
            shopName = shop?.shopName,
            shopType = shop?.shopType,
            impressHolder = ImpressHolder().apply { if (warrantyClaimButtonImpressed) invoke() }
        )
    }

    private fun mapInsuranceBmgm(
        productId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?
    ): ProductBmgmSectionUiModel.ProductUiModel.Insurance? {
        return insuranceDetailData?.protections?.let { protectionProducts ->
            protectionProducts.find {
                it?.productID == productId && it.isBundle != true
            }?.let { protectionProduct ->
                val iconUrl = protectionProduct.protectionConfig?.icon?.label
                val label = protectionProduct.protectionConfig?.wording?.id?.label
                if (iconUrl.isNullOrBlank() || label.isNullOrBlank()) {
                    null
                } else {
                    ProductBmgmSectionUiModel.ProductUiModel.Insurance(iconUrl, label)
                }
            }
        }
    }

    private fun mapInsurance(
        productId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?
    ): ProductListUiModel.ProductUiModel.Insurance? {
        return insuranceDetailData?.protections?.let { protectionProducts ->
            protectionProducts.find {
                it?.productID == productId && it.isBundle != true
            }?.let { protectionProduct ->
                val iconUrl = protectionProduct.protectionConfig?.icon?.label
                val label = protectionProduct.protectionConfig?.wording?.id?.label
                if (iconUrl.isNullOrBlank() || label.isNullOrBlank()) {
                    null
                } else {
                    ProductListUiModel.ProductUiModel.Insurance(iconUrl, label)
                }
            }
        }
    }

    private fun mapInsurance(
        productId: String,
        bundleId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?
    ): ProductListUiModel.ProductUiModel.Insurance? {
        return insuranceDetailData?.protections?.let { protectionProducts ->
            protectionProducts.find {
                it?.productID == productId && it.isBundle == true && it.bundleID == bundleId
            }?.let { protectionProduct ->
                val iconUrl = protectionProduct.protectionConfig?.icon?.label
                val label = protectionProduct.protectionConfig?.wording?.id?.label
                if (iconUrl.isNullOrBlank() || label.isNullOrBlank()) {
                    null
                } else {
                    ProductListUiModel.ProductUiModel.Insurance(iconUrl, label)
                }
            }
        }
    }

    private fun mapProductBmgmItem(
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bmgm.OrderDetail,
        orderId: String,
        orderStatusId: String,
        addOnLabel: String,
        addOnIcon: String,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        warrantyClaimButtonImpressed: Boolean,
        addOnsExpandableState: List<String>
    ): ProductBmgmSectionUiModel.ProductUiModel {
        val addOnIdentifier = generateAddOnsIdentifier(product.productId, product.orderDetailId)
        return ProductBmgmSectionUiModel.ProductUiModel(
            orderId = orderId,
            orderStatusId = orderStatusId,
            orderDetailId = product.orderDetailId,
            productId = product.productId,
            productName = product.productName,
            price = product.price,
            productPriceText = product.priceText,
            quantity = product.quantity,
            productNote = product.notes,
            categoryId = product.categoryId,
            category = product.category,
            thumbnailUrl = product.thumbnail,
            isProcessing = singleAtcResultFlow[product.productId] is AddToCartSingleRequestState.Requesting,
            insurance = null,
            button = mapActionButton(product.button),
            addOnSummaryUiModel = product.addonSummary?.let {
                AddOnSummaryUiModel(
                    addOnIdentifier = addOnIdentifier,
                    totalPriceText = if (it.totalPriceStr.isNotBlank()) {
                        StringRes(
                            order_management_commonR.string.om_add_on_collapsed_title_format,
                            listOf(it.totalPriceStr)
                        )
                    } else {
                        StringRes(Int.ZERO)
                    },
                    addonsLogoUrl = addOnIcon,
                    addonsTitle = addOnLabel,
                    addonItemList = it.addons?.map { addon ->
                        val addOnNote = addon.metadata?.addOnNote
                        val addOnInfoLink = addon.metadata?.infoLink
                        AddOnSummaryUiModel.AddonItemUiModel(
                            priceText = addon.priceStr,
                            quantity = addon.quantity,
                            addonsId = addon.id,
                            addOnsName = addon.name,
                            type = addon.type,
                            addOnsThumbnailUrl = addon.imageUrl,
                            toStr = addOnNote?.to.orEmpty(),
                            fromStr = addOnNote?.from.orEmpty(),
                            message = addOnNote?.notes.orEmpty(),
                            noteCopyable = false,
                            providedByShopItself = true,
                            infoLink = addOnInfoLink.orEmpty(),
                            tips = addOnNote?.tips.orEmpty(),
                            orderId = "",
                            orderDetailId = ""
                        )
                    }.orEmpty(),
                    canExpandCollapse = true
                ).also { addOnSummaryUiModel ->
                    addOnSummaryUiModel.isExpand = addOnsExpandableState.contains(addOnIdentifier)
                }
            },
            impressHolder = ImpressHolder().apply { if (warrantyClaimButtonImpressed) invoke() }
        )
    }

    private fun mapProductBundleItem(
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail,
        orderId: String,
        orderStatusId: String,
        bundleId: String,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        warrantyClaimButtonImpressed: Boolean
    ): ProductListUiModel.ProductUiModel {
        return ProductListUiModel.ProductUiModel(
            button = mapActionButton(product.button),
            category = product.category,
            categoryId = product.categoryId,
            orderDetailId = product.orderDetailId,
            orderId = orderId,
            orderStatusId = orderStatusId,
            price = product.price,
            priceText = product.priceText,
            productId = product.productId,
            productName = product.productName,
            productNote = product.notes,
            productThumbnailUrl = product.thumbnail,
            quantity = product.quantity,
            totalPrice = product.totalPrice,
            totalPriceText = product.totalPriceText,
            isProcessing = singleAtcResultFlow[product.productId] is AddToCartSingleRequestState.Requesting,
            insurance = mapInsurance(product.productId, bundleId, insuranceDetailData),
            productUrl = "",
            impressHolder = ImpressHolder().apply { if (warrantyClaimButtonImpressed) invoke() },
            addOnSummaryUiModel = mapProductBundleItemAddOn(product, addOnLabel, addOnIcon, addOnsExpandableState)
        )
    }

    private fun mapProductBundleItemAddOn(
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>
    ): AddOnSummaryUiModel? {
        return product.addonSummary?.let {
            val addOnsIdentifier = generateAddOnsIdentifier(product.productId, product.orderDetailId)
            AddOnSummaryUiModel(
                addOnIdentifier = addOnsIdentifier,
                totalPriceText = if (it.totalPriceStr.isNotBlank()) {
                    StringRes(
                        order_management_commonR.string.om_add_on_collapsed_title_format,
                        listOf(it.totalPriceStr)
                    )
                } else {
                    StringRes(Int.ZERO)
                },
                addonsLogoUrl = addOnIcon,
                addonsTitle = addOnLabel,
                addonItemList = it.addons?.map { addon ->
                    val addOnNote = addon.metadata?.addOnNote
                    val addOnInfoLink = addon.metadata?.infoLink
                    AddOnSummaryUiModel.AddonItemUiModel(
                        priceText = addon.priceStr,
                        quantity = addon.quantity,
                        addonsId = addon.id,
                        addOnsName = addon.name,
                        type = addon.type,
                        addOnsThumbnailUrl = addon.imageUrl,
                        toStr = addOnNote?.to.orEmpty(),
                        fromStr = addOnNote?.from.orEmpty(),
                        message = addOnNote?.notes.orEmpty(),
                        noteCopyable = false,
                        providedByShopItself = true,
                        infoLink = addOnInfoLink.orEmpty(),
                        tips = addOnNote?.tips.orEmpty(),
                        orderId = "",
                        orderDetailId = ""
                    )
                }.orEmpty(),
                canExpandCollapse = true
            ).also { addOnSummaryUiModel ->
                addOnSummaryUiModel.isExpand = addOnsExpandableState.contains(addOnsIdentifier)
            }
        }
    }

    private fun mapActionButton(
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button?
    ): ActionButtonsUiModel.ActionButton {
        return ActionButtonsUiModel.ActionButton(
            key = button?.key.orEmpty(),
            label = button?.displayName.orEmpty(),
            popUp = mapPopUp(button?.popup),
            variant = button?.variant.orEmpty(),
            type = button?.type.orEmpty(),
            url = button?.url.orEmpty()
        )
    }

    private fun mapPopUp(
        popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup?
    ): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
            actionButton = mapPopUpButtons(popup?.actionButton.orEmpty()),
            body = popup?.body.orEmpty(),
            title = popup?.title.orEmpty()
        )
    }

    private fun mapPopUpButtons(
        popUpButtons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton>
    ): List<ActionButtonsUiModel.ActionButton.PopUp.PopUpButton> {
        return popUpButtons.map {
            mapPopUpButton(it)
        }
    }

    private fun mapPopUpButton(
        popUpButton: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton
    ): ActionButtonsUiModel.ActionButton.PopUp.PopUpButton {
        return ActionButtonsUiModel.ActionButton.PopUp.PopUpButton(
            key = popUpButton.key,
            displayName = popUpButton.displayName,
            color = popUpButton.color,
            type = popUpButton.type,
            uriType = popUpButton.uriType,
            uri = popUpButton.uri
        )
    }

    private fun getAddonsSectionProductLevel(
        productId: String,
        orderDetailId: String,
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        addonSummary: AddonSummary?,
        addOnsExpandableState: List<String>
    ): AddonsListUiModel {
        val addOnsIdentifier = generateAddOnsIdentifier(productId, orderDetailId)
        return AddonsListUiModel(
            addOnIdentifier = addOnsIdentifier,
            addonsTitle = details.addonLabel,
            addonsLogoUrl = details.addonIcon,
            totalPriceText = StringRes(order_management_commonR.string.raw_string_format, listOf(addonSummary?.totalPriceStr.orEmpty())),
            addonsItemList = addonSummary?.addons?.map {
                val addonNote = it.metadata?.addOnNote
                val infoLink = it.metadata?.infoLink
                AddonsListUiModel.AddonItemUiModel(
                    priceText = it.priceStr,
                    addOnsName = it.name,
                    type = it.type,
                    addonsId = it.id,
                    quantity = it.quantity,
                    addOnsThumbnailUrl = it.imageUrl,
                    toStr = addonNote?.to.orEmpty(),
                    fromStr = addonNote?.from.orEmpty(),
                    message = addonNote?.notes.orEmpty(),
                    providedByShopItself = true,
                    infoLink = infoLink.orEmpty(),
                    tips = addonNote?.tips.orEmpty()
                )
            }.orEmpty(),
            canExpandCollapse = true,
            showTotalPrice = true
        ).also {
            it.isExpand = addOnsExpandableState.contains(addOnsIdentifier)
        }
    }

    private fun generateAddOnsIdentifier(
        productId: String,
        orderDetailId: String
    ) = productId + orderDetailId
}
