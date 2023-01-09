package com.tokopedia.mvc.presentation.intro.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class MvcIntroRecyclerViewScrollListener(
    private val layoutManager: LinearLayoutManager?
) : RecyclerView.OnScrollListener() {

    abstract fun changeBackground(position: Int)

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItem = layoutManager?.itemCount ?: return

        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        changeBackground(firstVisiblePosition)
    }
}
