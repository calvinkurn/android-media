package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.listener

import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OptionVariantUiModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface CheckoutVariantActionListener {

    fun onBindVariantGetProductViewModel(): OrderProduct

//    fun onBindVariantUpdateProductViewModel()

    fun onChangeVariant(selectedOptionUiModel: OptionVariantUiModel)

    fun onVariantGuidelineClick(variantGuideline: String)

}