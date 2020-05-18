package com.tokopedia.notifcenter.util

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify

fun RecyclerView.endLess(
        onScrolled: (dy: Int) -> Unit,
        onScrollStateChanged: (lastPosition: Int) -> Unit
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled(dy)
        }
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = (recyclerView.layoutManager)
                if (layoutManager != null && layoutManager is LinearLayoutManager) {
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    onScrollStateChanged(lastPosition)
                }
            }
        }
    })
}

fun View.resize(percentage: Int) {
    val displayMetrics = Resources.getSystem().displayMetrics
    val width = displayMetrics.widthPixels
    val params = this.layoutParams
    params.width = width * percentage / 100
    this.layoutParams = params
}

fun BottomSheetUnify.dialogWindow(): View? {
    return dialog?.window?.findViewById(android.R.id.content)
}