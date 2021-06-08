package com.tokopedia.minicart.cartlist

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.data.response.minicartlist.Button
import com.tokopedia.minicart.common.data.response.minicartlist.OutOfService
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setImage
import javax.inject.Inject

class GlobalErrorBottomSheet @Inject constructor() {

    fun show(fragmentManager: FragmentManager,
             context: Context,
             defaultType: Int = GlobalError.SERVER_ERROR,
             outOfService: OutOfService? = null,
             listener: GlobalErrorBottomSheetActionListener? = null) {

        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val view = View.inflate(context, R.layout.layout_bottomsheet_mini_cart_global_error, null)
        val layoutGlobalError: GlobalError = view.findViewById(R.id.layout_global_error)
        if (outOfService != null) {
            when (outOfService.id) {
                OutOfService.ID_MAINTENANCE, OutOfService.ID_TIMEOUT, OutOfService.ID_OVERLOAD -> {
                    layoutGlobalError.setType(GlobalError.SERVER_ERROR)
                    outOfService.buttons.firstOrNull()?.let { buttonData ->
                        layoutGlobalError.errorAction.text = buttonData.message
                        layoutGlobalError.setActionClickListener {
                            when (buttonData.id) {
                                Button.ID_START_SHOPPING, Button.ID_HOMEPAGE -> {
                                    listener?.onGoToHome()
                                    bottomSheet.dismiss()
                                }
                                Button.ID_RETRY -> {
                                    listener?.onRefreshErrorPage()
                                    bottomSheet.dismiss()
                                }
                            }
                        }
                    }
                }
            }

            if (outOfService.title.isNotBlank()) {
                layoutGlobalError.errorTitle.text = outOfService.title
            }
            if (outOfService.description.isNotBlank()) {
                layoutGlobalError.errorDescription.text = outOfService.description
            }
            if (outOfService.image.isNotBlank()) {
                layoutGlobalError.errorIllustration.setImage(outOfService.image, 0f)
            }
        } else {
            layoutGlobalError.setType(defaultType)
            layoutGlobalError.setActionClickListener {
                listener?.onRefreshErrorPage()
                bottomSheet.dismiss()
            }
        }

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, "Mini Cart Global Error")
    }
}

interface GlobalErrorBottomSheetActionListener {

    fun onGoToHome()

    fun onRefreshErrorPage()

}