package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recommendation_widget_common.databinding.ItemSpecBinding
import com.tokopedia.utils.view.binding.viewBinding

class SpecsItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private var binding: ItemSpecBinding? by viewBinding()
    fun bind(specsModel: SpecsModel, position: Int) {
        if (position == 0) binding?.viewDivider?.visibility = View.INVISIBLE
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