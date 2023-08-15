package com.tokopedia.catalogcommon.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemTopFeatureBinding
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class TopFeatureViewHolder(itemView: View) : AbstractViewHolder<TopFeaturesUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_top_feature
    }

    private val binding by viewBinding<WidgetItemTopFeatureBinding>()


    override fun bind(element: TopFeaturesUiModel) {
        for (item in element.items) {
            createItem(item)
        }
    }

    private fun createItem(item: TopFeaturesUiModel.ItemTopFeatureUiModel) {
        val linearLayout = createLinearLayout()
        val icon = createIcon(item.icon)
        val text = createTypography("tess")
        linearLayout.addView(icon)
        linearLayout.addView(text)
        binding?.lnRootUi?.addView(linearLayout)
    }

    private fun createLinearLayout(
        width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
        backgroundColor: Int = R.color.Unify_N150_20,
        borderColor: Int? = null
    ): LinearLayout {
        val linearLayout = LinearLayout(itemView.context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(width, height)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = android.view.Gravity.CENTER
        linearLayout.weightSum = 1f
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.RECTANGLE

        shapeDrawable.cornerRadius = 10f.dpToPx()
//        shapeDrawable.setStroke(2.dpToPx(), ContextCompat.getColor(this, R.color.border_color))

        // Set fill color
        shapeDrawable.setColor(ContextCompat.getColor(itemView.context, backgroundColor))

        return linearLayout
    }

    private fun createTypography(text: String): Typography {
        val displayMetrics = itemView.resources.displayMetrics

        val title = Typography(itemView.context)
        title.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        title.text = text
        title.textSize = 12f
        title.maxLines = 2
        title.setPadding(0, 0, 0, 16.dpToPx(displayMetrics))
        return title
    }

    private fun createIcon(image: String): ImageUnify {
        val icon = ImageUnify(itemView.context)
        icon.loadImage(image)
        return icon
    }
}
