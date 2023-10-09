package com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase

import com.tokopedia.shop.home.view.customview.directpurchase.ProductCardDirectPurchaseDataModel
import com.tokopedia.shop.home.view.model.viewholder.ShopDirectPurchaseByEtalaseUiModel

interface ShopHomeDirectPurchaseByEtalaseWidgetListener {
    fun onTriggerLoadProductDirectPurchaseWidget(
        etalaseId: String,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    )

    fun onClickAtcProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    )

    fun onImpressionDirectPurchaseByEtalaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        position: Int
    )

    fun onClickEtalaseGroupDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        widgetPosition: Int,
        selectedSwitcherIndex: Int,
    )

    fun onClickEtalaseDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        widgetPosition: Int,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    )

    fun onClickDirectPurchaseWidgetSeeMore(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    )
    fun onImpressionProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int,
        productPosition: Int
    )
    fun onClickProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int,
        productPosition: Int
    )

}
