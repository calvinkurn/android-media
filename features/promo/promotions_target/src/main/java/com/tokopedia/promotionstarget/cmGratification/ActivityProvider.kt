package com.tokopedia.promotionstarget.cmGratification

import android.app.Activity
import java.lang.ref.WeakReference

interface ActivityProvider {
    fun getActivity(): WeakReference<Activity?>?
}