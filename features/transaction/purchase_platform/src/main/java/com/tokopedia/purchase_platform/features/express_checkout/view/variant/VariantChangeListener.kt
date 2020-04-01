package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface VariantChangeListener {

    fun onSelectedVariantChanged(selectedVariant: OptionVariantUiModel)

}