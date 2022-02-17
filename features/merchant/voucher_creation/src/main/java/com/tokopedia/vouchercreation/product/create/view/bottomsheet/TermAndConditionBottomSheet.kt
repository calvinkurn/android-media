package com.tokopedia.vouchercreation.product.create.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R

class TermAndConditionBottomSheet : BottomSheetUnify() {

    private var termAndCondition = ""

    companion object {
        private const val BUNDLE_KEY_TERM_AND_CONDITION = "tnc"

        fun newInstance(context : Context, termAndConditions : String):  TermAndConditionBottomSheet{
            val args = Bundle()
            args.putString(BUNDLE_KEY_TERM_AND_CONDITION, termAndConditions)

            val view = View.inflate(context, R.layout.bottomsheet_coupon_tnc, null)

            val fragment = TermAndConditionBottomSheet().apply {
                arguments = args
                setChild(view)
                setTitle(context.getString(R.string.mvc_terms_and_condition_title).toBlankOrString())
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            }
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tpgTermAndCondition = view.findViewById<TextView>(R.id.tncDesc)
        termAndCondition = arguments?.getString(BUNDLE_KEY_TERM_AND_CONDITION).orEmpty()
        tpgTermAndCondition.text = termAndCondition
    }

}