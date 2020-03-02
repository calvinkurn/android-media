package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.listener

import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OptionVariantUiModel

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface VariantChangeListener {

    fun onSelectedVariantChanged(selectedVariant: OptionVariantUiModel)

}