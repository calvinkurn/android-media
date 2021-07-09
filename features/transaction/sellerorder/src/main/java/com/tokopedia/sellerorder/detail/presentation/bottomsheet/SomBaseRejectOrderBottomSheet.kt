package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.unifycomponents.Toaster

abstract class SomBaseRejectOrderBottomSheet(
        context: Context,
        childViewsLayoutResourceId: Int,
        bottomSheetTitle: String
) : SomBottomSheet(childViewsLayoutResourceId, true, true, false, bottomSheetTitle, context, true) {

    @SuppressLint("ClickableViewAccessibility")
    protected val hideKeyboardTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            childViews?.hideKeyboard()
        }
        false
    }

    protected fun checkReasonRejectIsNotEmpty(reason: String?): Boolean {
        var isNotEmpty = true
        if (reason.isNullOrEmpty()) isNotEmpty = false
        return isNotEmpty
    }

    protected fun showToasterError(message: String) {
        bottomSheetLayout?.parent.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
            }
        }
    }

    interface SomRejectOrderBottomSheetListener {
        fun onDoRejectOrder(orderRejectRequest: SomRejectRequestParam)
    }
}