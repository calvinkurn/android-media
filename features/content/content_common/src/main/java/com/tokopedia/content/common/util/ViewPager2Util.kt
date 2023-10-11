package com.tokopedia.content.common.util

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import timber.log.Timber

/**
 * Created by kenny.hadisaputra on 06/07/23
 */
fun ViewPager2.reduceDragSensitivity(f: Int = 4) {
    runCatching {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * f)
    }.onFailure {
        Timber.e(it)
    }
}
