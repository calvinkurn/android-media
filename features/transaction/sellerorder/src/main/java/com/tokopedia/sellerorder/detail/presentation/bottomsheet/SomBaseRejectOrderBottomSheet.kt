package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.unifycomponents.Toaster

abstract class SomBaseRejectOrderBottomSheet<T : ViewBinding>(
    context: Context,
    childViewsLayoutResourceId: Int,
    bottomSheetTitle: String
) : SomBottomSheet<T>(childViewsLayoutResourceId, true, true, false, false, false, bottomSheetTitle, context, true) {

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