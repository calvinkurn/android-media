package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState

object ProductListUiStateMapper {

    private fun mapProductListUiModel(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details?,
        bundleIcon: String,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?,
        orderId: String,
        orderStatusId: String,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): ProductListUiModel {
        val productList = details?.let {
            mapProductList(it, orderId, orderStatusId, singleAtcResultFlow)
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
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): List<ProductListUiModel.ProductUiModel> {
        return details.nonBundles?.map {
            mapProduct(
                details,
                it,
                it.addonSummary,
                orderId,
                orderStatusId,
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
            addonsListUiModel = getAddonsSectionProductLevel(details, addonSummary)
        )
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

    fun mapGetP0DataRequestStateToProductListUiState(
        getP0DataRequestState: GetP0DataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        ProductListUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        ProductListUiState.Showing(
                            mapProductListUiModel(
                                getBuyerOrderDetailRequestState.result.details,
                                getBuyerOrderDetailRequestState.result.details?.bundleIcon.orEmpty(),
                                getBuyerOrderDetailRequestState.result.shop,
                                getBuyerOrderDetailRequestState.result.addonInfo,
                                getBuyerOrderDetailRequestState.result.orderId,
                                getBuyerOrderDetailRequestState.result.orderStatus.id,
                                singleAtcRequestStates
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        ProductListUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                ProductListUiState.Showing(
                    mapProductListUiModel(
                        getBuyerOrderDetailRequestState.result.details,
                        getBuyerOrderDetailRequestState.result.details?.bundleIcon.orEmpty(),
                        getBuyerOrderDetailRequestState.result.shop,
                        getBuyerOrderDetailRequestState.result.addonInfo,
                        getBuyerOrderDetailRequestState.result.orderId,
                        getBuyerOrderDetailRequestState.result.orderStatus.id,
                        singleAtcRequestStates
                    )
                )
            }
            is GetP0DataRequestState.Error -> {
                ProductListUiState.Error(getP0DataRequestState.getThrowable())
            }
            else -> {
                ProductListUiState.Loading
            }
        }
    }
}
