package com.tokopedia.topads.view.sheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.constants.SpanConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.create.R
import com.tokopedia.topads.utils.Span
import com.tokopedia.topads.utils.SpannableUtils
import com.tokopedia.topads.utils.SpannedString
import com.tokopedia.unifyprinciples.Typography

class AdvertisingCostBottomSheet : BottomSheetUnify() {

    companion object{
        private const val BOLD_TEXT_1 = "di pencarian"
        private const val BOLD_TEXT_2 = "di rekomendasi"
        private const val BOTTOM_MARGIN = 16
    }

    private var infoTv:Typography?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitle(context?.resources?.getString(R.string.ad_group_stats_advertisement_costs_bottomsheet_title).orEmpty())
        infoTv = Typography(requireContext())
        setChild(infoTv)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheetText(){
        val content = context?.getString(R.string.ad_group_stats_advertisement_costs_bottomsheet_content).orEmpty()
        val textColor = ResourcesCompat.getColor(requireContext().resources,com.tokopedia.unifyprinciples.R.color.Unify_NN950,null)
        infoTv?.apply {
            text = SpannableUtils.applySpannable(
                content,
                SpannedString(BOLD_TEXT_1, listOf(
                    Span(SpanConstant.TYPEFACE_SPAN,Typeface.BOLD)
                )),
                SpannedString(BOLD_TEXT_2, listOf(
                    Span(SpanConstant.TYPEFACE_SPAN,Typeface.BOLD)
                ))
            )
            setType(Typography.PARAGRAPH_2)
            setTextColor(textColor)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetText()
        val lp = infoTv?.layoutParams as? LinearLayout.LayoutParams
        lp?.bottomMargin = context?.dpToPx(BOTTOM_MARGIN)?.toInt().orZero()
    }

}
