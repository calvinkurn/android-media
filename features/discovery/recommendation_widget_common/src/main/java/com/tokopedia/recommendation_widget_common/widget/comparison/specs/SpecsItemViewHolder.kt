package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.recommendation_widget_common.databinding.ItemSpecBinding
import com.tokopedia.recommendation_widget_common.viewutil.parseColorHex
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class SpecsItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private var binding: ItemSpecBinding? by viewBinding()
    fun bind(specsModel: SpecsModel, position: Int) {
        binding?.viewDivider?.visibility = View.INVISIBLE
        binding?.tvSpecTitle?.text = specsModel.specsTitle
        binding?.tvSpecSummary?.text = MethodChecker.fromHtml(specsModel.specsSummary)

        val textColor = specsModel.colorConfig.textColor.parseColorHex(
            ContextCompat.getColor(view.context, unifyprinciplesR.color.Unify_NN950_96)
        )
        binding?.tvSpecTitle?.setTextColor(textColor)
        binding?.tvSpecSummary?.setTextColor(textColor)

        val drawable = ContextCompat.getDrawable(view.context, specsModel.bgDrawableRef)
        drawable?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(drawable),
                specsModel.colorConfig.anchorBackgroundColor.parseColorHex( ContextCompat.getColor(view.context, specsModel.bgDrawableColorRef) )
            )
        }
        binding?.holderSpecs?.background = drawable
    }
}
