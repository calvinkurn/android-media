package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topads.create.R

class AdGroupStatisticsBottomSheet : BottomSheetUnify() {

    companion object{
        private const val BOTTOM_MARGIN = 16
    }

    private var infoTv:Typography?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        infoTv = Typography(requireContext())
        setTitle(context?.getString(R.string.ad_group_stats_bottomsheet_title).orEmpty())
        setChild(infoTv)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoTv?.apply {
            text = context?.getString(R.string.ad_group_stats_bottomsheet_text).orEmpty()
            setType(Typography.PARAGRAPH_2)
            setTextColor(ResourcesCompat.getColor(requireContext().resources,com.tokopedia.unifyprinciples.R.color.Unify_NN950,null))
            val lp = layoutParams as? LinearLayout.LayoutParams
            lp?.bottomMargin = context?.dpToPx(BOTTOM_MARGIN)?.toInt().orZero()
        }
    }

}
