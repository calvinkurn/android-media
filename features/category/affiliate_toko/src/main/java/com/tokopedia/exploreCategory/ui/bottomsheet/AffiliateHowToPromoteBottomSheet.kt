package com.tokopedia.exploreCategory.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateHowToPromoteBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
    private val steps: ArrayList<String> = arrayListOf()

    companion object {

        fun newInstance(): AffiliateHowToPromoteBottomSheet {
            return AffiliateHowToPromoteBottomSheet().apply {
                arguments = Bundle().apply {
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
        setTitle(getString(R.string.affiliate_how_to_promote))
        contentView = View.inflate(context,
                R.layout.affiliate_how_to_promote_bottom_sheet, null)
        steps.add(getString(R.string.affiliate_how_to_get_link))
        steps.add(getString(R.string.affiliate_how_to_get_link_1))
        steps.add(getString(R.string.affiliate_how_to_get_link_2))
        steps.add(getString(R.string.affiliate_how_to_get_link_3))
        steps.add(getString(R.string.affiliate_how_to_get_link_4))
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
