package com.tokopedia.tokochat_common.view.customview.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.databinding.TokochatGlobalErrorBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoChatErrorBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatGlobalErrorBinding>()

    init {
        this.showCloseIcon = false
    }

    private var errorType: Int = GlobalError.SERVER_ERROR
    private var buttonAction: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.tokochat_common.R.layout.tokochat_global_error, container, false)
        binding = TokochatGlobalErrorBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.tokochatGlobalError?.setType(errorType)
        binding?.tokochatGlobalError?.setActionClickListener {
            buttonAction?.invoke()
        }
        binding?.tokochatLayoutGlobalError?.show()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun dismissBottomSheet() {
        view?.post {
            this.dismiss()
        }
    }

    fun setButtonAction(buttonAction: () -> Unit) {
        this.buttonAction = buttonAction
    }

    fun setErrorType(errorType: Int) {
        this.errorType = errorType
    }

    companion object {
        private val TAG = TokoChatErrorBottomSheet::class.simpleName
    }
}

