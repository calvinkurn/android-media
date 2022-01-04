package com.tokopedia.product.manage.feature.list.view.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler

/**
 * This class is created as a pragmatic, but a bad way to solve IndexOutOfBoundsException crashes.
 * We will keep this class while deploying blind fixes for current problem and monitor those.
 * After there are no more crashes, we will remove this
 */
class ProductManageLayoutManager(context: Context) : LinearLayoutManager(context, VERTICAL, false) {

    companion object {
        private const val ERROR_MESSAGE = "Error Product Manage onLayoutChildren"
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ex: Exception) {
            ProductManageListErrorHandler.logExceptionToCrashlytics(ex, ERROR_MESSAGE)
        }
    }

}