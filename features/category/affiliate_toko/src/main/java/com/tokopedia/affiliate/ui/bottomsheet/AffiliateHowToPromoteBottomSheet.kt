package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateHowToPromoteBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
    private val steps: ArrayList<String> = arrayListOf()
    private var state = STATE_HOW_TO_PROMOTE

    companion object {
        const val STATE = "state"
        const val STATE_HOW_TO_PROMOTE  = 1
        const val STATE_PRODUCT_INACTIVE = 2
        const val STATE_BETA_INFO = 3

        fun newInstance(state : Int): AffiliateHowToPromoteBottomSheet {
            return AffiliateHowToPromoteBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(STATE,state)
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
                steps.add(getString(R.string.affiliate_how_to_get_link))
                steps.add(getString(R.string.affiliate_how_to_get_link_1))
                steps.add(getString(R.string.affiliate_how_to_get_link_2))
                steps.add(getString(R.string.affiliate_how_to_get_link_3))
                steps.add(getString(R.string.affiliate_how_to_get_link_4))
            }
            STATE_PRODUCT_INACTIVE -> {
                setTitle(getString(R.string.affiliate_product_inactive))
                steps.add(getString(R.string.affiliate_product_inactive_text))
            }
            else -> {
                setTitle(getString(R.string.affiliate_beta_info))
                steps.add(getString(R.string.affiliate_beta_info_text))
            }
        }
        steps.add("")
        contentView?.findViewById<LinearLayout>(R.id.affiliate_parent_linear)?.let { linearLayout ->
            linearLayout.removeAllViews()
            for (step in steps) {
                val typography = Typography(requireContext()).apply {
                    this.setWeight(Typography.REGULAR)
                    this.setType(Typography.BODY_2)
                    this.setTextColor(MethodChecker.getColor(requireContext(), R.color.Unify_N700_96))
                    this.setPadding(
                            0,
                            resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                            0,
                            0)
                }
                typography.text = step
                linearLayout.addView(typography)
            }
        }
        setChild(contentView)
    }
}
