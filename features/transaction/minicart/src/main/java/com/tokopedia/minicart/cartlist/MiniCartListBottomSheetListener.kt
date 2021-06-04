package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.common.widget.GlobalEvent

interface MiniCartListBottomSheetListener {

    fun onMiniCartListBottomSheetDismissed()

    fun onFailedUpdateCartForCheckout(toasterAnchorView: View, context: Context, fragmentManager: FragmentManager, globalEvent: GlobalEvent)

}