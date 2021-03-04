package com.tokopedia.promotionstarget.cm

import android.app.Activity
import java.lang.ref.WeakReference

interface ActivityProvider {
    fun getActivity(): WeakReference<Activity?>?
}