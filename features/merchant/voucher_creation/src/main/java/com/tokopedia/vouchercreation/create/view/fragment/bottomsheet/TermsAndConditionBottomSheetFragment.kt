package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R

class TermsAndConditionBottomSheetFragment(context: Context) : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context) : TermsAndConditionBottomSheetFragment = TermsAndConditionBottomSheetFragment(context).apply {
            val view = View.inflate(context, R.layout.bottomsheet_mvc_tnc, null)
            setChild(view)
            setTitle(context.getString(R.string.mvc_terms_and_condition_title).toBlankOrString())
        }

        val TAG = TermsAndConditionBottomSheetFragment::javaClass.name
    }

}