package com.tokopedia.tokomember_seller_dashboard.util

import android.content.res.Resources


fun dpToPx(dp: Int): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}