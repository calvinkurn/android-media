package com.tokopedia.recommendation_widget_common.widget.comparison2.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsMapper
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsModel
import kotlinx.android.synthetic.main.item_spec2.view.*

class Specs2ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(specsModel: SpecsModel, position: Int, currentRecommendationPosition: Int, totalRecommendations: Int) {
        if(position == 0) view.view_divider.visibility = View.INVISIBLE
        else if(currentRecommendationPosition == 1)
            view.view_divider.setMargin(view.context.resources.getDimensionPixelSize(R.dimen.dimen_difference_card_use_compat_padding_view_divider),0,0,0)
        else if(currentRecommendationPosition == totalRecommendations-1)
            view.view_divider.setMargin(0,0,view.context.resources.getDimensionPixelSize(R.dimen.dimen_difference_card_use_compat_padding_view_divider),0)
        view.tv_spec_title.text = specsModel.specsTitle
        view.tv_spec_summary.text = MethodChecker.fromHtml(specsModel.specsSummary)

        val drawable = ContextCompat.getDrawable(view.context, specsModel.bgDrawableRef)
        drawable?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(drawable),
                ContextCompat.getColor(view.context, specsModel.bgDrawableColorRef)
            )
        }
        view.holder_specs.background = drawable
    }
}