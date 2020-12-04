package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.getSpannableString
import kotlinx.android.synthetic.main.bottomsheet_mvc_review_back.view.*

class ChangeDetailPromptBottomSheetFragment : BottomSheetUnify(){

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           onCancelVoucher: () -> Unit): ChangeDetailPromptBottomSheetFragment {
            return ChangeDetailPromptBottomSheetFragment().apply {
                val view = View.inflate(context, R.layout.bottomsheet_mvc_review_back, null)
                setChild(view)
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                this.onCancelVoucher = onCancelVoucher
            }
        }

        const val TAG = "ChangeDetailPromptBottomSheet"
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            initView()
        }
    }

    fun setBackButtonClickListener(action: () -> Unit) {
        onBackButtonClicked = action
    }

    private fun initView() {
        view?.run {
            reviewBackConfirmButton?.setOnClickListener {
                onBackButtonClicked()
                dismiss()
            }
            reviewBackCancelText?.run {
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
}