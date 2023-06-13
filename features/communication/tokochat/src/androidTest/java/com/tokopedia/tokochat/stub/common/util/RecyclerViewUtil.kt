package com.tokopedia.tokochat.stub.common.util

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtil {

    fun <T> getAdapter(activity: Activity, recyclerViewId: Int): T {
        val recyclerView = activity.findViewById(recyclerViewId) as? RecyclerView
        return recyclerView?.adapter as T
    }
}
