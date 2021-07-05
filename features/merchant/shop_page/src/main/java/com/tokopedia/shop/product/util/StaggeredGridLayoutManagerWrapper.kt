package com.tokopedia.shop.product.util

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.ERROR_RECYCLER_VIEW

class StaggeredGridLayoutManagerWrapper(spanCount: Int, orientation: Int) : StaggeredGridLayoutManager(spanCount, orientation) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            ShopPageExceptionHandler.logExceptionToCrashlytics(ERROR_RECYCLER_VIEW, e)
        }
    }

    override fun onScrollStateChanged(state: Int) {
        try {
            super.onScrollStateChanged(state)
        } catch (e: Exception) {
            ShopPageExceptionHandler.logExceptionToCrashlytics(ERROR_RECYCLER_VIEW, e)
        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return try {
            return super.scrollVerticallyBy(dy, recycler, state)
        } catch (e: Exception) {
            ShopPageExceptionHandler.logExceptionToCrashlytics(ERROR_RECYCLER_VIEW, e)
            0
        }
    }
}