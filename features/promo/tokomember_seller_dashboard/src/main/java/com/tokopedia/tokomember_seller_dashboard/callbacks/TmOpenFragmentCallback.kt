package com.tokopedia.tokomember_seller_dashboard.callbacks

import android.os.Bundle

interface TmOpenFragmentCallback {
    fun openFragment(screenType: Int, bundle: Bundle)
}