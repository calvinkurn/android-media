package com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.getSpannableString
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcReviewBackBinding

class ChangeDetailPromptBottomSheetFragment : BottomSheetUnify(){

    companion object {
        @JvmStatic
        fun createInstance(onCancelVoucher: () -> Unit): ChangeDetailPromptBottomSheetFragment {
            return ChangeDetailPromptBottomSheetFragment().apply {
                this.onCancelVoucher = onCancelVoucher
            }
        }

        const val TAG = "ChangeDetailPromptBottomSheet"
    }

    private var binding by autoClearedNullable<BottomsheetMvcReviewBackBinding>()

    private var onCancelVoucher: () -> Unit = {}

    private val cancelDescription by lazy {
        context?.getString(R.string.mvc_review_back_redirect).toBlankOrString()
    }
    private val clickableSpan by lazy {
        context?.getString(R.string.mvc_review_back_redirect_clickable).toBlankOrString()
    }
    private val spanColor by lazy {
        context?.getResColor(com.tokopedia.unifyprinciples.R.color.light_G500).toZeroIfNull()
    }

    private var onBackButtonClicked: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    private fun iniBottomSheet() {
        binding = BottomsheetMvcReviewBackBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private fun initView() {
        view?.run {
            binding?.reviewBackConfirmButton?.setOnClickListener {
                onBackButtonClicked()
                dismiss()
            }
            binding?.reviewBackCancelText?.run {
                text = getSpannableString(spanColor, cancelDescription, clickableSpan, ::onClickCancelVoucher)
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun onClickCancelVoucher() {
        this.dismiss()
        onCancelVoucher()
    }

    fun setBackButtonClickListener(action: () -> Unit) {
        onBackButtonClicked = action
    }
}