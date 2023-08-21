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
import com.tokopedia.buyerorderdetail.presentation.model.StringRes
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel

object ProductListUiStateMapper {

    private const val MAX_PRODUCT_WHEN_COLLAPSED = 1
    private const val MAX_UNFULFILLED_PRODUCT_WHEN_COLLAPSED = 0

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ProductListUiState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean
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
                    collapseProductList
                )
            }
        }
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
        collapseProductList: Boolean
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
                    collapseProductList
                )
            }
            is GetInsuranceDetailRequestState.Complete -> {
                mapOnGetInsuranceDetailComplete(
                    buyerOrderDetailData,
                    insuranceDetailRequestState,
                    singleAtcRequestStates,
                    collapseProductList
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
        collapseProductList: Boolean
    ): ProductListUiState {
        return if (currentState is ProductListUiState.HasData) {
            mapOnReloading(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                currentState,
                singleAtcRequestStates,
                collapseProductList
            )
        } else {
            mapOnDataReady(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                singleAtcRequestStates,
                collapseProductList
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
        collapseProductList: Boolean
    ): ProductListUiState {
        return mapOnDataReady(
            buyerOrderDetailData,
            insuranceDetailRequestState,
            singleAtcRequestStates,
            collapseProductList
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
        collapseProductList: Boolean
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
                collapseProductList
            )
        )
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean
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
                collapseProductList
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
        collapseProductList: Boolean
    ): ProductListUiModel {
        /**
         * Map product bmgm response into UI model and limit the number of mapped items based on
         * MAX_PRODUCT_WHEN_COLLAPSED. The numOfRemovedProductBundle is indicating the number of
         * unmapped product bundle which will be used on the toggle view to show remaining hidden product
         * when the product list view is collapsed. The productBundlingList contains the UI models
         * which limited by the MAX_PRODUCT_WHEN_COLLAPSED.
         */
        val (numOfRemovedProductBmgm, productBmgmList) = mapProductBmgm(
            details?.bmgms,
            details?.bmgmIcon.orEmpty(),
            orderId,
            details?.addonLabel.orEmpty(),
            details?.addonIcon.orEmpty(),
            collapseProductList,
            MAX_PRODUCT_WHEN_COLLAPSED
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
            orderId,
            orderStatusId,
            insuranceDetailData,
            singleAtcResultFlow,
            collapseProductList,
            MAX_PRODUCT_WHEN_COLLAPSED - productBmgmList.size
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
            mapProductList(
                details = it,
                orderId = orderId,
                orderStatusId = orderStatusId,
                insuranceDetailData = insuranceDetailData,
                singleAtcResultFlow = singleAtcResultFlow,
                collapseProductList = collapseProductList,
                remainingSlot = MAX_PRODUCT_WHEN_COLLAPSED - productBmgmList.size - productBundlingList.size,
                isPof = false
            )
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
                isPof = true
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
                numOfRemovedAddOnsList = numOfRemovedAddOn,
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
        numOfRemovedAddOnsList: Int,
        numOfRemovedUnFulfilledProduct: Int
    ): ProductListUiModel.ProductListToggleUiModel? {
        return if (collapseProductList) {
            val numOfRemovedItems =
                numOfRemovedProductBmgm + numOfRemovedProductBundle + numOfRemovedNonProductBundle + numOfRemovedAddOnsList + numOfRemovedUnFulfilledProduct
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
        remainingSlot: Int
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
                details,
                it,
                it.addonSummary,
                orderId,
                orderStatusId,
                isPof,
                insuranceDetailData,
                singleAtcResultFlow
            )
        }.orEmpty()
        return numOfRemovedNonBundles to mappedNonBundles
    }

    @Suppress("SameParameterValue")
    private fun mapProductBundle(
        bundleDetail: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle>?,
        bundleIcon: String,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>,
        collapseProductList: Boolean,
        remainingSlot: Int
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
                        insuranceDetailData,
                        singleAtcResultFlow
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
        addOnLabel: String,
        addOnIcon: String,
        collapseProductList: Boolean,
        remainingSlot: Int
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
                totalPriceReductionInfoText = bmgm.tierDiscountAmountFormatted,
                bmgmItemList = bmgm.orderDetail.map { orderDetail ->
                    mapProductBmgmItem(
                        orderDetail,
                        orderId,
                        addOnLabel,
                        addOnIcon
                    )
                }
            )
        }.orEmpty()
        return numOfRemovedBmgmDetail to mappedProductBmgm
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
        val mappedAddOn = addonInfo?.let { addonInfo ->
            AddonsListUiModel(
                addonsTitle = addonInfo.label,
                addonsLogoUrl = addonInfo.iconUrl,
                totalPriceText = addonInfo.orderLevel?.totalPriceStr.orEmpty(),
                addonsItemList = addonInfo.orderLevel?.addons?.map {
                    val addonNote = it.metadata?.addonNote
                    AddonsListUiModel.AddonItemUiModel(
                        priceText = it.priceStr,
                        addOnsName = it.name,
                        type = it.type,
                        addonsId = it.id,
                        quantity = it.quantity,
                        addOnsThumbnailUrl = it.imageUrl,
                        toStr = addonNote?.to.orEmpty(),
                        fromStr = addonNote?.from.orEmpty(),
                        message = addonNote?.notes.orEmpty()
                    )
                }.orEmpty()
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
        remainingSlot: Int
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
                details,
                it,
                it.addonSummary,
                orderId,
                orderStatusId,
                isPof,
                insuranceDetailData,
                singleAtcResultFlow
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
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
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
            addonsListUiModel = getAddonsSectionProductLevel(details, addonSummary),
            insurance = mapInsurance(product.productId, insuranceDetailData),
            isPof = isPof
        )
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
        addOnLabel: String,
        addOnIcon: String
    ): ProductBmgmSectionUiModel.ProductUiModel {
        return ProductBmgmSectionUiModel.ProductUiModel(
            orderId = orderId,
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
            button = mapActionButton(product.button),
            addOnSummaryUiModel = product.addonSummary?.let {
                com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel(
                    totalPriceText = it.totalPriceStr,
                    addonsLogoUrl = addOnIcon,
                    addonsTitle = addOnLabel,
                    addonItemList = it.addons?.map { addon ->
                        val addOnNote = addon.metadata?.addOnNote
                        com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel.AddonItemUiModel(
                            priceText = addon.priceStr,
                            quantity = addon.quantity,
                            addonsId = addon.id,
                            addOnsName = addon.name,
                            type = addon.type,
                            addOnsThumbnailUrl = addon.imageUrl,
                            toStr = addOnNote?.to.orEmpty(),
                            fromStr = addOnNote?.from.orEmpty(),
                            message = addOnNote?.notes.orEmpty(),
                            hasShop = false
                        )
                    }.orEmpty()
                )
            }
        )
    }

    private fun mapProductBundleItem(
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail,
        orderId: String,
        orderStatusId: String,
        bundleId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
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
            insurance = mapInsurance(product.productId, bundleId, insuranceDetailData)
        )
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
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        addonSummary: AddonSummary?
    ): AddonsListUiModel {
        return AddonsListUiModel(
            addonsTitle = details.addonLabel,
            addonsLogoUrl = details.addonIcon,
            totalPriceText = addonSummary?.totalPriceStr.orEmpty(),
            addonsItemList = addonSummary?.addons?.map {
                val addonNote = it.metadata?.addOnNote
                AddonsListUiModel.AddonItemUiModel(
                    priceText = it.priceStr,
                    addOnsName = it.name,
                    type = it.type,
                    addonsId = it.id,
                    quantity = it.quantity,
                    addOnsThumbnailUrl = it.imageUrl,
                    toStr = addonNote?.to.orEmpty(),
                    fromStr = addonNote?.from.orEmpty(),
                    message = addonNote?.notes.orEmpty()
                )
            }.orEmpty()
        )
    }
}
