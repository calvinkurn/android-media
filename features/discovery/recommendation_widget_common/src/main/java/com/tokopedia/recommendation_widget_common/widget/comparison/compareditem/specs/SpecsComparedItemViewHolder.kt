package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemSpecComparedItemBinding
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsModel
import com.tokopedia.utils.view.binding.viewBinding

class SpecsComparedItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private var binding: ItemSpecComparedItemBinding? by viewBinding()
    fun bind(specsModel: SpecsModel, position: Int, currentRecommendationPosition: Int, totalRecommendations: Int) {
        when {
            position == 0 -> binding?.viewDivider?.visibility = View.INVISIBLE
            currentRecommendationPosition == 1 -> binding?.viewDivider?.setMargin(view.context.resources.getDimensionPixelSize(R.dimen.dimen_difference_card_use_compat_padding_view_divider),0,0,0)
            currentRecommendationPosition == totalRecommendations-1 -> binding?.viewDivider?.setMargin(0,0,view.context.resources.getDimensionPixelSize(R.dimen.dimen_difference_card_use_compat_padding_view_divider),0)
        }
        binding?.tvSpecTitle?.text = specsModel.specsTitle
        binding?.tvSpecSummary?.text = MethodChecker.fromHtml(specsModel.specsSummary)

        val drawable = ContextCompat.getDrawable(view.context, specsModel.bgDrawableRef)
        drawable?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(drawable),
                ContextCompat.getColor(view.context, specsModel.bgDrawableColorRef)
            )
        }
        binding?.holderSpecs?.background = drawable
    }
}