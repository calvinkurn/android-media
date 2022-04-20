package com.tokopedia.oldminicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.oldminicart.common.widget.GlobalEvent

interface MiniCartListBottomSheetListener {

    fun onMiniCartListBottomSheetDismissed()

    fun onBottomSheetSuccessGoToCheckout()

    fun onBottomSheetFailedGoToCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent)

    fun showToaster(view: View? = null, message: String, type: Int, ctaText: String = "Oke", isShowCta: Boolean = true, onClickListener: View.OnClickListener? = null)

    fun showProgressLoading()

    fun hideProgressLoading()
}