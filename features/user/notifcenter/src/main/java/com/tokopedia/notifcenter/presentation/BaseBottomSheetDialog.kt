package com.tokopedia.notifcenter.presentation

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheetDialog<T>(context: Context, fragmentManager: FragmentManager) {

    private var bottomSheet: BottomSheetUnify = BottomSheetUnify()
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
        bottomSheet.setChild(onCreateView(context))
        bottomSheet.showHeader = true
        bottomSheet.isFullpage = true
        bottomSheet.showCloseIcon = true
        bottomSheet.show(fragmentManager, BOTTOM_SHEET_TAG)
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }

    companion object {
        private const val BOTTOM_SHEET_TAG = "notificationDetailPage"
    }

}