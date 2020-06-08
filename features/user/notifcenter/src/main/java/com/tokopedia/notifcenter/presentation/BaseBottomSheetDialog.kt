package com.tokopedia.notifcenter.presentation

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheetDialog<T>(context: Context, fragmentManager: FragmentManager) {

    protected var bottomSheet: BottomSheetUnify = BottomSheetUnify()
    protected var container: View? = null

    abstract fun resourceId(): Int
    abstract fun show(element: T)

    init {
        onViewCreated(context, fragmentManager)
    }

    private fun onCreateView(context: Context): View? {
        container = View.inflate(context, resourceId(), null)
        return container
    }

    private fun onViewCreated(context: Context, fragmentManager: FragmentManager) {
        with(bottomSheet) {
            showCloseIcon = true
            showHeader = true
            setChild(onCreateView(context))
            show(fragmentManager, BOTTOM_SHEET_TAG)
            setCloseClickListener { dismiss() }
        }
    }

    protected fun setFullPage(isFullPage: Boolean) {
        bottomSheet.isFullpage = isFullPage
    }

    companion object {
        private const val BOTTOM_SHEET_TAG = "notificationDetailPage"
    }

}