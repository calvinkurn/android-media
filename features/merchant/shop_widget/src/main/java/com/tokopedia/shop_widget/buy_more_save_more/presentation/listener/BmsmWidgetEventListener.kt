package com.tokopedia.shop_widget.buy_more_save_more.presentation.listener

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product

interface BmsmWidgetEventListener {
    fun onBmsmWidgetSuccessAtc(
        offerId: String,
        offerType: String,
        productId: String,
        result: AddToCartDataModel
    )

    fun onBmsmWidgetErrorAtc(errorMessage: String)

    fun onBmsmWidgetNavigateToOlp(
        offerId: String,
        offerType: String,
        applink: String,
        parentPosition: Int
    )

    fun onBmsmWidgetProductClicked(
        offerId: String,
        offerType: String,
        product: Product
    )

    fun onImpressBmsmWidget(
        offerId: String,
        parentPosition: Int
    )

    fun onSelectTabBmsmWidget(
        offerId: String,
        offerType: String,
        parentPosition: Int
    )
}
