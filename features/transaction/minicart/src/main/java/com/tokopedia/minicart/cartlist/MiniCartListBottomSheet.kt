package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.R
import com.tokopedia.unifycomponents.BottomSheetUnify

object MiniCartListBottomSheet {

    fun show(fragmentManager: FragmentManager, context: Context) {
        val bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            setTitle("Title")
            showHeader = true
            isDragable = true
            showKnob = true
            isHideable = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        }

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_list, null)

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, this.javaClass.simpleName)
    }

}