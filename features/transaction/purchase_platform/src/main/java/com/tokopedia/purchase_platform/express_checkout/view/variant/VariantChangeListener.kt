package com.tokopedia.purchase_platform.express_checkout.view.variant

import com.tokopedia.purchase_platform.express_checkout.view.variant.viewmodel.OptionVariantViewModel

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface VariantChangeListener {

    fun onSelectedVariantChanged(selectedVariant: OptionVariantViewModel)

}