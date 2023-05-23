package com.tokopedia.buyerorderdetail.domain.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBomGroupedOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BaseOwocSectionGroupUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BaseOwocVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocSectionGroupUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

class GetBomGroupedOrderMapper @Inject constructor() {

    fun mapToOwocGroupedOrderWrapper(getBomGroupedOrder: GetBomGroupedOrderResponse.GetBomGroupedOrder): OwocGroupedOrderWrapper {
        val owocGroupedOrderList = mutableListOf<BaseOwocSectionGroupUiModel>().apply {
            mapToTickerUiModel(getBomGroupedOrder.ticker)?.let {
                add(it)
            }
            addAll(
                mapToOwocBaseOwocSectionGroupUiModel(
                    getBomGroupedOrder.orders
                )
            )
        }

        return OwocGroupedOrderWrapper(owocGroupedOrderList, getBomGroupedOrder.title)
    }

    private fun mapToTickerUiModel(tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo?): OwocTickerUiModel? {
        return tickerInfo?.let {
            OwocTickerUiModel(
                actionKey = tickerInfo.actionKey,
                actionText = tickerInfo.actionText,
                actionUrl = tickerInfo.actionUrl,
                description = tickerInfo.text,
                type = tickerInfo.type
            )
        }
    }

    private fun mapToOwocBaseOwocSectionGroupUiModel(
        orders: List<GetBomGroupedOrderResponse.GetBomGroupedOrder.Order>
    ): List<BaseOwocSectionGroupUiModel> {
        return orders.mapIndexed { index, it ->
            OwocSectionGroupUiModel(
                baseOwocProductListUiModel = mutableListOf<BaseOwocVisitableUiModel>().apply {
                    add(mapToProductListHeaderUiModel(it))

                    val (productBundlingListToShow, productBundlingRest) = mapToSplitProductBundleList(
                        it
                    )

                    var remainingSlot = if (productBundlingListToShow.size >= MAX_ORDER_ITEMS) {
                        Int.ZERO
                    } else {
                        MAX_ORDER_ITEMS - productBundlingListToShow.size
                    }

                    if (productBundlingListToShow.isNotEmpty()) {
                        addAll(productBundlingListToShow)
                    }

                    val (productNonBundlingListToShow, productNonBundlingRest) = mapToSplitProductNonBundleList(
                        it,
                        remainingSlot
                    )

                    remainingSlot = if (productNonBundlingListToShow.size >= remainingSlot) {
                        Int.ZERO
                    } else {
                        remainingSlot - productNonBundlingListToShow.size
                    }

                    if (productNonBundlingListToShow.isNotEmpty()) {
                        addAll(productNonBundlingListToShow)
                    }

                    val (addonListToShow, addonListRest) = mapToSplitAddonList(
                        it,
                        remainingSlot
                    )

                    if (addonListToShow.addonsItemList.isNotEmpty()) {
                        add(addonListToShow)
                    }

                    val groupedProductListUiModelRest = mutableListOf<BaseOwocVisitableUiModel>().apply {
                        if (productBundlingRest.isNotEmpty()) {
                            addAll(productBundlingRest)
                        }
                        if (productNonBundlingRest.isNotEmpty()) {
                            addAll(productNonBundlingRest)
                        }
                        if (addonListRest.addonsItemList.isNotEmpty()) {
                            add(addonListRest)
                        }
                    }

                    if (groupedProductListUiModelRest.isNotEmpty()) {
                        add(
                            mapToProductListToggleUiModel(
                                remainingProductList = groupedProductListUiModelRest
                            )
                        )
                    }

                    if (index < orders.size - Int.ONE) {
                        add(OwocThickDividerUiModel())
                    }
                }
            )
        }
    }

    private fun mapToSplitProductBundleList(
        order: GetBomGroupedOrderResponse.GetBomGroupedOrder.Order
    ): Pair<List<OwocProductListUiModel.ProductBundlingUiModel>, List<OwocProductListUiModel.ProductBundlingUiModel>> {
        val orderDetails = order.details

        val (productBundlingListToShow, productBundlingRest) = if (orderDetails.bundle?.size.orZero() >= MAX_ORDER_ITEMS) {
            orderDetails.bundle?.take(MAX_ORDER_ITEMS).orEmpty() to orderDetails.bundle?.slice(
                MAX_ORDER_ITEMS..orderDetails.bundle.size - Int.ONE
            ).orEmpty()
        } else {
            orderDetails.bundle.orEmpty() to emptyList()
        }

        val mapProductBundlingListToShow = mapToProductBundleListUiModel(
            orderId = order.orderId,
            bundleIcon = orderDetails.bundleIcon,
            bundles = productBundlingListToShow
        )

        val mapProductBundlingListRest = if (productBundlingRest.isNotEmpty()) {
            mapToProductBundleListUiModel(
                orderId = order.orderId,
                bundleIcon = orderDetails.bundleIcon,
                bundles = productBundlingRest
            )
        } else {
            emptyList()
        }

        return Pair(mapProductBundlingListToShow, mapProductBundlingListRest)
    }

    private fun mapToSplitProductNonBundleList(
        order: GetBomGroupedOrderResponse.GetBomGroupedOrder.Order,
        remainingOrderTotal: Int
    ): Pair<List<OwocProductListUiModel.ProductUiModel>,
        List<OwocProductListUiModel.ProductUiModel>> {
        val orderDetails = order.details

        val (productNonBundlingListToShow, productNonBundlingListRest) = if (orderDetails.nonBundle?.size.orZero() >= remainingOrderTotal) {
            orderDetails.nonBundle?.take(remainingOrderTotal).orEmpty() to orderDetails.nonBundle?.slice(
                remainingOrderTotal..orderDetails.nonBundle.size - Int.ONE
            ).orEmpty()
        } else {
            orderDetails.nonBundle.orEmpty() to emptyList()
        }

        val mapProductNonBundlingListToShow = mapToProductNonBundleItemUiModel(
            orderId = order.orderId,
            addonLabel = orderDetails.addonLabel,
            addonIcon = orderDetails.addonIcon,
            detailsNonBundle = productNonBundlingListToShow
        )

        val mapProductNonBundlingListRest = mapToProductNonBundleItemUiModel(
            orderId = order.orderId,
            addonLabel = orderDetails.addonLabel,
            addonIcon = orderDetails.addonIcon,
            detailsNonBundle = productNonBundlingListRest
        )

        return Pair(mapProductNonBundlingListToShow, mapProductNonBundlingListRest)
    }

    private fun mapToSplitAddonList(
        order: GetBomGroupedOrderResponse.GetBomGroupedOrder.Order,
        remainingOrderTotal: Int
    ): Pair<OwocAddonsListUiModel, OwocAddonsListUiModel> {
        val orderDetails = order.details

        val (addonListToShow, addonListRest) = if (orderDetails.orderAddons?.size.orZero() >= remainingOrderTotal) {
            orderDetails.orderAddons?.take(remainingOrderTotal) to orderDetails.orderAddons?.slice(
                remainingOrderTotal..orderDetails.orderAddons.size - Int.ONE
            )
        } else {
            orderDetails.orderAddons to emptyList()
        }

        val mapAddonListToShow = mapToAddonsSection(
            addonLabel = orderDetails.addonLabel,
            addonIcon = orderDetails.addonIcon,
            addonList = addonListToShow
        )

        val mapAddonListToRest = mapToAddonsSection(
            addonLabel = orderDetails.addonLabel,
            addonIcon = orderDetails.addonIcon,
            addonList = addonListRest
        )

        return Pair(mapAddonListToShow, mapAddonListToRest)
    }

    private fun mapToProductBundleListUiModel(
        orderId: String,
        bundleIcon: String,
        bundles: List<GetBomGroupedOrderResponse.GetBomGroupedOrder.Order.Details.Bundle>
    ): List<OwocProductListUiModel.ProductBundlingUiModel> {
        return bundles.map { bundle ->
            OwocProductListUiModel.ProductBundlingUiModel(
                bundleId = bundle.bundleId,
                bundleName = bundle.bundleName,
                bundleIconUrl = bundleIcon,
                bundleItemList = mapToProductBundleItemUiModel(
                    orderId = orderId,
                    orderDetail = bundle.orderDetail
                )
            )
        }
    }

    fun mapToProductListHeaderUiModel(
        order: GetBomGroupedOrderResponse.GetBomGroupedOrder.Order
    ): OwocProductListUiModel.ProductListHeaderUiModel {
        val shop = order.shop
        val actionButton = order.button
        return OwocProductListUiModel.ProductListHeaderUiModel(
            shopBadgeUrl = shop.badgeUrl,
            shopName = shop.shopName,
            invoiceNumber = order.invoice,
            orderId = order.orderId,
            currentShopId = shop.shopId,
            owocActionButtonUiModel = OwocProductListUiModel.ProductListHeaderUiModel.OwocActionButtonUiModel(
                key = actionButton.key,
                displayName = actionButton.displayName,
                variant = actionButton.variant,
                type = actionButton.type,
                url = actionButton.url
            )
        )
    }

    private fun mapToProductNonBundleItemUiModel(
        orderId: String,
        addonLabel: String,
        addonIcon: String,
        detailsNonBundle: List<GetBomGroupedOrderResponse.GetBomGroupedOrder.Order.Details.NonBundle>
    ): List<OwocProductListUiModel.ProductUiModel> {
        return detailsNonBundle.map {
            OwocProductListUiModel.ProductUiModel(
                orderDetailId = it.orderDetailId,
                orderId = orderId,
                priceText = it.priceText,
                productId = it.productId,
                productName = it.productName,
                productThumbnailUrl = it.thumbnail,
                quantity = it.quantity,
                addonsListUiModel = mapToAddonsSection(
                    addonLabel,
                    addonIcon,
                    it.addon
                )
            )
        }
    }

    private fun mapToProductBundleItemUiModel(
        orderId: String,
        orderDetail: List<GetBomGroupedOrderResponse.GetBomGroupedOrder.Order.Details.Bundle.OrderDetail>
    ): List<OwocProductListUiModel.ProductUiModel> {
        return orderDetail.map {
            OwocProductListUiModel.ProductUiModel(
                orderDetailId = it.orderDetailId,
                orderId = orderId,
                priceText = it.priceText,
                productId = it.productId,
                productName = it.productName,
                productThumbnailUrl = it.thumbnail,
                quantity = it.quantity
            )
        }
    }

    private fun mapToAddonsSection(
        addonLabel: String,
        addonIcon: String,
        addonList: List<GetBomGroupedOrderResponse.GetBomGroupedOrder.Order.Details.Addon>?
    ): OwocAddonsListUiModel {
        return OwocAddonsListUiModel(
            addonsTitle = addonLabel,
            addonsLogoUrl = addonIcon,
            addonsItemList = addonList?.map {
                AddonsListUiModel.AddonItemUiModel(
                    priceText = it.price,
                    addOnsName = it.name,
                    type = it.type,
                    addonsId = it.id,
                    quantity = it.quantity,
                    addOnsThumbnailUrl = it.imageUrl,
                    toStr = String.EMPTY,
                    fromStr = String.EMPTY,
                    message = String.EMPTY
                )
            }.orEmpty()
        )
    }

    private fun mapToProductListToggleUiModel(
        remainingProductList: List<BaseOwocVisitableUiModel>
    ): OwocProductListUiModel.ProductListToggleUiModel {
        return OwocProductListUiModel.ProductListToggleUiModel(
            isExpanded = false,
            remainingProductList = remainingProductList
        )
    }

    companion object {
        const val MAX_ORDER_ITEMS = 3
    }
}
