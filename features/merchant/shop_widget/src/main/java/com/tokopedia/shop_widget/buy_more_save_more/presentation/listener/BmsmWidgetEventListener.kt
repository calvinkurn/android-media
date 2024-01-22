package com.tokopedia.shop_widget.buy_more_save_more.presentation.listener

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product

interface BmsmWidgetEventListener {
    fun onBmsmWidgetSuccessAtc(result: AddToCartDataModel)

    fun onBmsmWidgetErrorAtc(errorMessage: String)

    fun onBmsmWidgetNavigateToOlp(applink: String)

    fun onBmsmWidgetProductClicked(product: Product)
}
