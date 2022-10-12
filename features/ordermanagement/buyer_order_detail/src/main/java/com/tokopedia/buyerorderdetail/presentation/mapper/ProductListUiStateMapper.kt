package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState

object ProductListUiStateMapper {

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState
        return when (val p0DataRequestState = buyerOrderDetailDataRequestState.getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                mapOnP0Requesting(p0DataRequestState, p1DataRequestState, singleAtcRequestStates)
            }
            is GetP0DataRequestState.Success -> {
                mapOnP0Success(p0DataRequestState, p1DataRequestState, singleAtcRequestStates)
            }
            is GetP0DataRequestState.Error -> {
                mapOnP0Error(p0DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): ProductListUiState {
        return mapOnLoading()
    }

    private fun mapOnP0Requesting(
        p0DataRequestState: GetP0DataRequestState.Requesting,
        p1DataRequestState: GetP1DataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (
            val getBuyerOrderDetailRequestState = p0DataRequestState.getBuyerOrderDetailRequestState
        ) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                mapOnGetBuyerOrderDetailIsSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState,
                    singleAtcRequestStates
                )
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                mapOnError(getBuyerOrderDetailRequestState.throwable)
            }
        }
    }

    private fun mapOnP0Success(
        p0DataRequestState: GetP0DataRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return mapOnGetBuyerOrderDetailIsSuccess(
            p0DataRequestState.getBuyerOrderDetailRequestState,
            p1DataRequestState,
            singleAtcRequestStates
        )
    }

    private fun mapOnP0Error(
        p0DataRequestState: GetP0DataRequestState.Error
    ): ProductListUiState {
        return mapOnError(p0DataRequestState.getThrowable())
    }

    private fun mapOnGetBuyerOrderDetailIsSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(
                    buyerOrderDetailRequestState,
                    p1DataRequestState,
                    singleAtcRequestStates
                )
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(
                    buyerOrderDetailRequestState,
                    p1DataRequestState.getInsuranceDetailRequestState,
                    singleAtcRequestStates
                )
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            else -> {
                mapOnDataReady(
                    buyerOrderDetailRequestState.result,
                    p1DataRequestState.getInsuranceDetailRequestState,
                    singleAtcRequestStates
                )
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return mapOnDataReady(
            buyerOrderDetailRequestState.result,
            insuranceDetailRequestState,
            singleAtcRequestStates
        )
    }

    private fun mapOnLoading(): ProductListUiState {
        return ProductListUiState.Loading
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        val insuranceDetailData = insuranceDetailRequestState.let {
            if (it is GetInsuranceDetailRequestState.Success) {
                it.result
            } else null
        }
        return ProductListUiState.Showing(
            mapProductListUiModel(
                buyerOrderDetailData.details,
                buyerOrderDetailData.details?.bundleIcon.orEmpty(),
                buyerOrderDetailData.shop,
                buyerOrderDetailData.addonInfo,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                insuranceDetailData,
                singleAtcRequestStates
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable
    ): ProductListUiState {
        return ProductListUiState.Error(throwable)
    }

    private fun mapProductListUiModel(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details?,
        bundleIcon: String,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): ProductListUiModel {
        val productList = details?.let {
            mapProductList(it, orderId, orderStatusId, insuranceDetailData, singleAtcResultFlow)
        }.orEmpty()
        val productBundlingList = mapProductBundle(
            details?.bundles,
            bundleIcon,
            orderId,
            orderStatusId,
            singleAtcResultFlow
        )
        return ProductListUiModel(
            productList = productList,
            productListHeaderUiModel = mapProductListHeaderUiModel(shop, orderId, orderStatusId),
            productBundlingList = productBundlingList,
            addonsListUiModel = getAddonsSectionOrderLevel(addonInfo)
        )
    }

    private fun mapProductList(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): List<ProductListUiModel.ProductUiModel> {
        return details.nonBundles?.map {
            mapProduct(
                details,
                it,
                it.addonSummary,
                orderId,
                orderStatusId,
                insuranceDetailData,
                singleAtcResultFlow
            )
        }.orEmpty()
    }

    private fun mapProductBundle(
        bundleDetail: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle>?,
        bundleIcon: String,
        orderId: String,
        orderStatusId: String,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): List<ProductListUiModel.ProductBundlingUiModel> {
        return bundleDetail?.map { bundle ->
            ProductListUiModel.ProductBundlingUiModel(
                bundleName = bundle.bundleName,
                bundleIconUrl = bundleIcon,
                totalPrice = bundle.bundleSubtotalPrice,
                totalPriceText = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                bundleItemList = bundle.orderDetail.map { bundleDetail ->
                    mapProductBundleItem(bundleDetail, orderId, orderStatusId, singleAtcResultFlow)
                }
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

    private fun getAddonsSectionOrderLevel(
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?
    ): AddonsListUiModel? {
        return if (addonInfo != null) {
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
        } else null
    }

    private fun mapProduct(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details,
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle,
        addonSummary: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle.AddonSummary?,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
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
            insurance = mapInsurance(product.productId, insuranceDetailData)
        )
    }

    private fun mapInsurance(
        productId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
    ): ProductListUiModel.ProductUiModel.Insurance? {
        return insuranceDetailData?.protectionProduct?.protections?.let { protectionProducts ->
            protectionProducts.find {
                it?.productID == productId
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

    private fun mapProductBundleItem(
        product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail,
        orderId: String,
        orderStatusId: String,
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
            isProcessing = singleAtcResultFlow[product.productId] is AddToCartSingleRequestState.Requesting
        )
    }

    private fun mapActionButton(button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button?): ActionButtonsUiModel.ActionButton {
        return ActionButtonsUiModel.ActionButton(
            key = button?.key.orEmpty(),
            label = button?.displayName.orEmpty(),
            popUp = mapPopUp(button?.popup),
            variant = button?.variant.orEmpty(),
            type = button?.type.orEmpty(),
            url = button?.url.orEmpty()
        )
    }

    private fun mapPopUp(popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup?): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
            actionButton = mapPopUpButtons(popup?.actionButton.orEmpty()),
            body = popup?.body.orEmpty(),
            title = popup?.title.orEmpty()
        )
    }

    private fun mapPopUpButtons(popUpButtons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton>): List<ActionButtonsUiModel.ActionButton.PopUp.PopUpButton> {
        return popUpButtons.map {
            mapPopUpButton(it)
        }
    }

    private fun mapPopUpButton(popUpButton: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton): ActionButtonsUiModel.ActionButton.PopUp.PopUpButton {
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
        addonSummary: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle.AddonSummary?
    ): AddonsListUiModel {
        return AddonsListUiModel(
            addonsTitle = details.addonLabel,
            addonsLogoUrl = details.addonIcon,
            totalPriceText = addonSummary?.totalPriceStr.orEmpty(),
            addonsItemList = addonSummary?.addons?.map {
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

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (getBuyerOrderDetailDataRequestState) {
            is GetBuyerOrderDetailDataRequestState.Started -> {
                mapOnGetBuyerOrderDetailDataStarted(
                    getBuyerOrderDetailDataRequestState,
                    singleAtcRequestStates
                )
            }
            else -> {
                mapOnGetBuyerOrderDetailIdling()
            }
        }
    }
}
