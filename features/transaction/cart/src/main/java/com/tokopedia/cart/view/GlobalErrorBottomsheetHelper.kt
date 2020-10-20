package com.tokopedia.cart.view

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.layout_bottomsheet_global_error.view.*

fun showGlobalErrorBottomsheet(fragmentManager: FragmentManager, context: Context, actionCallback: () -> Unit) {

    val bottomSheet = BottomSheetUnify()
    bottomSheet.showCloseIcon = true
    bottomSheet.showHeader = true

    val view = View.inflate(context, R.layout.layout_bottomsheet_global_error, null)
    view.layout_global_error.setActionClickListener {
        actionCallback()
        bottomSheet.dismiss()
    }

    bottomSheet.setChild(view)
    bottomSheet.show(fragmentManager, "Cart Global Error")

}