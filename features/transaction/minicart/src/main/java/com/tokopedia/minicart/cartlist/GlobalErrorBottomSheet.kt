package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.minicart.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class GlobalErrorBottomSheet @Inject constructor() {

    companion object {
        const val TYPE_FAILED_TO_LOAD = 1
        const val TYPE_NO_CONNECTION = 2
        const val TYPE_OVERLOAD = 3
        const val TYPE_DOWN = 4
        const val TYPE_MAINTENANCE = 5
    }

    fun show(fragmentManager: FragmentManager, context: Context, type: Int, actionCallback: () -> Unit) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_global_error, null)
        val layoutGlobalError: GlobalError = view.findViewById(R.id.layout_global_error)
        when (type) {
            TYPE_FAILED_TO_LOAD -> {
                layoutGlobalError.setType(GlobalError.NO_CONNECTION)
            }
            TYPE_NO_CONNECTION -> {
                layoutGlobalError.setType(GlobalError.NO_CONNECTION)
            }
            TYPE_OVERLOAD -> {
                layoutGlobalError.setType(GlobalError.PAGE_FULL)
            }
            TYPE_DOWN -> {
                layoutGlobalError.setType(GlobalError.SERVER_ERROR)
            }
            TYPE_MAINTENANCE -> {
                layoutGlobalError.setType(GlobalError.MAINTENANCE)
            }
        }
        layoutGlobalError.setActionClickListener {
            actionCallback()
            bottomSheet.dismiss()
        }

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, "Mini Cart Global Error")

    }
}