package com.tokopedia.affiliate.ui.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AFFILIATE_LIHAT_KATEGORI
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography


class AffiliateHowToPromoteBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
    private val steps: ArrayList<Pair<String,Boolean>> = arrayListOf()
    private var state = STATE_HOW_TO_PROMOTE
    private var performanceTitle = ""
    private var performanceDesc = ""

    companion object {
        const val TITILE = "title"
        const val DESC = "description"
        const val STATE = "state"
        const val STATE_HOW_TO_PROMOTE  = 1
        const val STATE_PRODUCT_INACTIVE = 2
        const val STATE_BETA_INFO = 3
        const val STATE_PERFORMA_INFO = 4

        fun newInstance(state : Int,title: String? = "",description: String? = null): AffiliateHowToPromoteBottomSheet {
            return AffiliateHowToPromoteBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(STATE,state)
                    putString(TITILE,title)
                    putString(DESC,description)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
                R.layout.affiliate_how_to_promote_bottom_sheet, null)
        arguments?.let {
            state = it.getInt(STATE)
        }
        when (state) {
            STATE_HOW_TO_PROMOTE -> {
                setTitle(getString(R.string.affiliate_how_to_promote))
                steps.add(Pair(getString(R.string.affiliate_how_to_get_link),false))
                steps.add(Pair(getString(R.string.affiliate_how_to_get_link_1),true))
                steps.add(Pair(getString(R.string.affiliate_how_to_get_link_2),false))
                steps.add(Pair(getString(R.string.affiliate_how_to_get_link_3),false))
                steps.add(Pair(getString(R.string.affiliate_how_to_get_link_4),false))
            }
            STATE_PRODUCT_INACTIVE -> {
                setTitle(getString(R.string.affiliate_product_inactive))
                steps.add(Pair(getString(R.string.affiliate_product_inactive_text),false))
            }
            STATE_PERFORMA_INFO -> {
                arguments?.let {
                    performanceTitle = it.getString(TITILE,"")
                    performanceDesc = it.getString(DESC,"")
                }
                setTitle(performanceTitle)
                steps.add(Pair(performanceDesc,false))
            }
            else -> {
                setTitle(getString(R.string.affiliate_beta_info))
                steps.add(Pair(getString(R.string.affiliate_beta_info_text),false))
            }
        }
        steps.add(Pair("",false))
        contentView?.findViewById<LinearLayout>(R.id.affiliate_parent_linear)?.let { linearLayout ->
            linearLayout.removeAllViews()
            for (step in steps) {
                val typography = Typography(requireContext()).apply {
                    this.setWeight(Typography.REGULAR)
                    this.setType(Typography.BODY_2)
                    this.setTextColor(MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    this.setPadding(
                            0,
                            resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                            0,
                            0)
                }
                if (step.second){
                    getSpannableString(step.first,typography)
                }else
                    typography.text = step.first
                linearLayout.addView(typography)
            }
        }
        setChild(contentView)
    }

    private fun getSpannableString(string : String, typography: Typography) {
        val spannableString = SpannableString(string)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                dismiss()
                RouteManager.routeNoFallbackCheck(context, AFFILIATE_LIHAT_KATEGORI, AFFILIATE_LIHAT_KATEGORI)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = MethodChecker.getColor(requireContext(),com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.isUnderlineText = false
            }
        }
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 137, 151, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, 137, 151, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        typography.text = spannableString
        typography.movementMethod = LinkMovementMethod.getInstance()
    }
}
