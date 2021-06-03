package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.minicart.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class GlobalErrorBottomSheet @Inject constructor() {

    fun show(fragmentManager: FragmentManager, context: Context, actionCallback: () -> Unit) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_global_error, null)
        val layoutGlobalError: GlobalError = view.findViewById(R.id.layout_global_error)
        layoutGlobalError.setActionClickListener {
            actionCallback()
            bottomSheet.dismiss()
        }

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, "Mini Cart Global Error")

    }
}