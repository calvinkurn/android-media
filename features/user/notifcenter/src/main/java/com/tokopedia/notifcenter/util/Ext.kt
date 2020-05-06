package com.tokopedia.notifcenter.util

import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
        provider: ViewModelProvider.Factory
) = ViewModelProviders.of(this, provider).get(VM::class.java)

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

inline fun <reified T> List<T>.isSingleItem(): Boolean = this.size == 1

fun View.resize(percentage: Int) {
    val displayMetrics = Resources.getSystem().displayMetrics
    val width = displayMetrics.widthPixels
    val params = this.layoutParams
    params.width = width * percentage / 100
    this.layoutParams = params
}