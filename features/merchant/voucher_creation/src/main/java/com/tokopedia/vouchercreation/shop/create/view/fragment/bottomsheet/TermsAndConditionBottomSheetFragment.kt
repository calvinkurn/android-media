package com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcTncBinding

class TermsAndConditionBottomSheetFragment : BottomSheetUnify() {
    companion object {
        @JvmStatic
        fun createInstance(context: Context) : TermsAndConditionBottomSheetFragment = TermsAndConditionBottomSheetFragment().apply {
            setChild(binding?.root)
            setTitle(context.getString(R.string.mvc_terms_and_condition_title).toBlankOrString())
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        const val TAG = "TermsAndConditionBottomSheet"
    }

    private var binding by autoClearedNullable<BottomsheetMvcTncBinding>()

    var tncContent: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (tncContent.isNotBlank()) binding?.tncDesc?.text = MethodChecker.fromHtml(tncContent)
    }

    fun setHtmlTncDesc(htmlTncDesc: String) {
        tncContent = htmlTncDesc
    }
}