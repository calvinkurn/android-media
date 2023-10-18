package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class RecommendationInfoBottomSheet : BottomSheetUnify() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(
                paddingLeft, paddingTop, paddingRight,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
            )
        }
        context?.let {
            val title = Typography(it).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text = resources.getString(R.string.topads_dashboard_recommendation_info_sheet_sub_title)
                setType(Typography.HEADING_4)
            }
            val description = Typography(it).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text = resources.getString(R.string.topads_dashboard_recommendation_info_sheet_desc)
                setType(Typography.BODY_2)
                setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            }
            layout.addView(title)
            layout.addView(description)
        }

        setChild(layout)

        isDragable = true
        isHideable = true
        showKnob = true
        showCloseIcon = false
        setTitle(resources.getString(R.string.topads_dashboard_recommendation_info_sheet_title))
    }
}