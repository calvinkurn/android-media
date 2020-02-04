package com.tokopedia.atc_variant.view

import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel

/**
 * Created by Irfan Khoirul on 03/12/18.
 */

interface VariantChangeListener {

    fun onSelectedVariantChanged(selectedVariant: OptionVariantViewModel)

}