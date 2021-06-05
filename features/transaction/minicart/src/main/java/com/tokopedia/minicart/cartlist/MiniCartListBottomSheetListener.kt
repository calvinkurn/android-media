package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.common.widget.GlobalEvent

interface MiniCartListBottomSheetListener {

    fun onMiniCartListBottomSheetDismissed()

    fun onBottomSheetSuccessUpdateCartForCheckout()

    fun onBottomSheetFailedUpdateCartForCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent)

    fun showToaster(view: View?, message: String, type: Int, ctaText: String = "Oke", onClickListener: View.OnClickListener? = null)


}