package com.tokopedia.tokofood.purchase.purchasepage.presentation.subview

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.cartcommon.data.response.common.Button
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseGlobalErrorBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setImage

class TokoFoodPurchaseGlobalErrorBottomSheet : BottomSheetUnify() {

    var viewBinding: LayoutBottomSheetPurchaseGlobalErrorBinding? = null

    companion object {
        @JvmStatic
        fun createInstance(globalErrorType: Int,
                           listener: Listener?): TokoFoodPurchaseGlobalErrorBottomSheet {
            return TokoFoodPurchaseGlobalErrorBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(GLOBAL_ERROR_TYPE, globalErrorType)
                }
                this.listener = listener
            }
        }

        private const val GLOBAL_ERROR_TYPE = "error_type"

        private const val TAG = "TokoFoodPurchaseGlobalErrorBottomSheet"
    }

    interface Listener {
        fun onGoToHome()
        fun onRetry()
        fun onCheckOtherMerchant()
        fun onStayOnCurrentPage()
    }

    private val globalErrorType by lazy {
        arguments?.getInt(GLOBAL_ERROR_TYPE) ?: GlobalError.SERVER_ERROR
    }

    private var listener: Listener? = null

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            renderGlobalError(it, globalErrorType)
        }
    }

    private fun initializeView(): LayoutBottomSheetPurchaseGlobalErrorBinding {
        val viewBinding = LayoutBottomSheetPurchaseGlobalErrorBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseGlobalErrorBinding) {
        showCloseIcon = true
        showHeader = true
        isDragable = false
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun renderGlobalError(viewBinding: LayoutBottomSheetPurchaseGlobalErrorBinding,
                                  globalErrorType: Int) {
        with(viewBinding) {
            layoutGlobalError.setType(globalErrorType)
            layoutGlobalError.setButtonFull(true)
            if (globalErrorType == GlobalError.NO_CONNECTION) {
                layoutGlobalError.setActionClickListener {
                    dismiss()
                    listener?.onRetry()
                }
            } else {
                layoutGlobalError.errorAction.text = "Ke Homepage"
                layoutGlobalError.setActionClickListener {
                    dismiss()
                    listener?.onGoToHome()
                }
            }
        }
    }

    // TODO: Remove if it does not need out of service
    private fun renderGlobalError(viewBinding: LayoutBottomSheetPurchaseGlobalErrorBinding,
                                  outOfService: OutOfService?,
                                  listener: Listener?,
                                  defaultType: Int) {
        with(viewBinding) {
            if (outOfService != null) {
                when (outOfService.id) {
                    OutOfService.ID_MAINTENANCE, OutOfService.ID_TIMEOUT, OutOfService.ID_OVERLOAD -> {
                        layoutGlobalError.setType(GlobalError.SERVER_ERROR)
                        outOfService.buttons.firstOrNull()?.let { buttonData ->
                            layoutGlobalError.errorAction.text = buttonData.message
                            layoutGlobalError.setActionClickListener {
                                when (buttonData.id) {
                                    Button.ID_HOMEPAGE -> {
                                        listener?.onGoToHome()
                                        dismiss()
                                    }
                                    Button.ID_RETRY -> {
                                        listener?.onRetry()
                                        dismiss()
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
                    listener?.onRetry()
                    dismiss()
                }
            }
        }
    }
}