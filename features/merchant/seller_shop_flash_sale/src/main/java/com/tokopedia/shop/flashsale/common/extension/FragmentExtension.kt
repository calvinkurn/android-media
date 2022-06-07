package com.tokopedia.shop.flashsale.common.extension

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.setFragmentToUnifyBgColor() {
    if (activity != null && context != null) {
        activity!!.window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                context!!, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
}