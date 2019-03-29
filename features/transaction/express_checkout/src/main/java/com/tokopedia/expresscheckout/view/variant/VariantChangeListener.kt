package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface VariantChangeListener {

    fun onSelectedVariantChanged(selectedVariant: OptionVariantViewModel)

}