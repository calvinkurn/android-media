package com.tokopedia.product_bundle.common.extension

import android.app.Activity
import androidx.core.content.ContextCompat

internal fun Activity?.setBackgroundToWhite() {
    this?.apply {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }
}