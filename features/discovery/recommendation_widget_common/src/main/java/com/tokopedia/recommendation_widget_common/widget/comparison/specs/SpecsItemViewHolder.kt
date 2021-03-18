package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.item_spec.view.*

class SpecsItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val context = view.context

    fun bind(specsModel: SpecsModel) {
        view.tv_spec_title.text = specsModel.specsTitle
        view.tv_spec_summary.text = specsModel.specsSummary

        val drawable = ContextCompat.getDrawable(context, specsModel.bgDrawableRef)
        drawable?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(drawable),
                ContextCompat.getColor(context, specsModel.bgDrawableColorRef)
            );
        }
        view.holder_specs.background = drawable

    }
}