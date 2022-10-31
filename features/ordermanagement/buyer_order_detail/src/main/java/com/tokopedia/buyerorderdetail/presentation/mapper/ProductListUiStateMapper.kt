package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object ProductListUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ProductListUiState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
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
                    singleAtcRequestStates
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
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return when (val insuranceDetailRequestState = p1DataRequestState.getInsuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(
                    buyerOrderDetailData,
                    insuranceDetailRequestState,
                    currentState,
                    singleAtcRequestStates
                )
            }
            is GetInsuranceDetailRequestState.Complete -> {
                mapOnGetInsuranceDetailComplete(
                    buyerOrderDetailData,
                    insuranceDetailRequestState,
                    singleAtcRequestStates
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
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return if (currentState is ProductListUiState.HasData) {
            mapOnReloading(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                currentState,
                singleAtcRequestStates
            )
        } else {
            mapOnDataReady(
                buyerOrderDetailData,
                insuranceDetailRequestState,
                singleAtcRequestStates
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
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return mapOnDataReady(
            buyerOrderDetailData,
            insuranceDetailRequestState,
            singleAtcRequestStates
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
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
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
                singleAtcRequestStates
            )
        )
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailRequestState: GetInsuranceDetailRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        val insuranceDetailData = insuranceDetailRequestState.let {
            if (it is GetInsuranceDetailRequestState.Complete.Success) {
                it.result?.protectionProduct
            } else null
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
                singleAtcRequestStates
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

    private fun mapProductListUiModel(
        details: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details?,
        bundleIcon: String,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        addonInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.AddonInfo?,
        orderId: String,
        orderStatusId: String,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
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
            insuranceDetailData,
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
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
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
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data.ProtectionProduct?,
        singleAtcResultFlow: Map<String, AddToCartSingleRequestState>
    ): List<ProductListUiModel.ProductBundlingUiModel> {
        return bundleDetail?.map { bundle ->
            ProductListUiModel.ProductBundlingUiModel(
                bundleId = bundle.bundleId,
                bundleName = bundle.bundleName,
                bundleIconUrl = bundleIcon,
                totalPrice = bundle.bundleSubtotalPrice,
                totalPriceText = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                bundleItemList = bundle.orderDetail.map { bundleDetail ->
                    mapProductBundleItem(bundleDetail, orderId, orderStatusId, bundle.bundleId, insuranceDetailData, singleAtcResultFlow)
                })
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
            insurance = mapInsurance(product.productId, insuranceDetailData)
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
}
