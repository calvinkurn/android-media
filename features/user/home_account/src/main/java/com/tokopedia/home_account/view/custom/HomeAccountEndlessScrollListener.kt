package com.tokopedia.home_account.view.custom

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener

abstract class HomeAccountEndlessScrollListener(layoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener(layoutManager) {
    fun changeLoadingStatus(status: Boolean) {
        loading = status
    }
}