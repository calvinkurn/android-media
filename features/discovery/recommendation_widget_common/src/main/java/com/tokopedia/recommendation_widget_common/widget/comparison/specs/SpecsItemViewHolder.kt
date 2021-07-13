package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import kotlinx.android.synthetic.main.item_spec.view.*

class SpecsItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(specsModel: SpecsModel, position: Int) {
        if(position == 0) view.view_divider.visibility = View.INVISIBLE
        view.tv_spec_title.text = specsModel.specsTitle
        view.tv_spec_summary.text = specsModel.specsSummary

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