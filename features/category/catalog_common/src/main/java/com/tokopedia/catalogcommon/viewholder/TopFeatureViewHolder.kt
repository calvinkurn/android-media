package com.tokopedia.catalogcommon.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemTopFeatureBinding
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding


class TopFeatureViewHolder(itemView: View) : AbstractViewHolder<TopFeaturesUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_top_feature
    }

    private val binding by viewBinding<WidgetItemTopFeatureBinding>()
    private val displayMetrics = itemView.resources.displayMetrics


    override fun bind(element: TopFeaturesUiModel) {
        binding?.lnRootUi?.setBackgroundColor(element.widgetBackgroundColor.orDefaultColor(itemView.context))
        element.items.forEachIndexed { index, item ->
            createItem(item)
            if (index < element.items.size - 1) {
                binding?.lnRootUi?.addView(Divider())
            }
        }
    }

    private fun createItem(item: TopFeaturesUiModel.ItemTopFeatureUiModel) {
        val linearLayout = createLinearLayout(
            backgroundColor = item.backgroundColor,
            borderColor = item.borderColor
        )
        val icon = createIcon(item.icon)
        val text = createTypography(item.name, item.textColor)
        linearLayout.addView(icon)
        linearLayout.addView(text)
        binding?.lnRootUi?.addView(linearLayout)
    }

    private fun createLinearLayout(
        width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
        backgroundColor: Int? = null,
        cornerRadius: Float = 12f.dpToPx(),
        borderColor: Int? = null
    ): LinearLayout {
        val linearLayout = LinearLayout(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(width, height)
        layoutParam.weight = 1f
        linearLayout.layoutParams = layoutParam
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.RECTANGLE

        shapeDrawable.cornerRadius = cornerRadius
        shapeDrawable.setStroke(
            1.dpToPx(displayMetrics),
            borderColor.orDefaultColor(itemView.context)
        )
        shapeDrawable.setColor(backgroundColor.orDefaultColor(itemView.context))
        linearLayout.background = shapeDrawable
        linearLayout.setPadding(0, 8.dpToPx(displayMetrics), 0, 8.dpToPx(displayMetrics))
        return linearLayout
    }

    private fun createTypography(text: String, textColor: Int?): Typography {
        val title = Typography(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParam.gravity = Gravity.CENTER
        title.layoutParams = layoutParam
        title.text = text
        title.setTextColor(textColor.orDefaultColor(itemView.context))
        title.textSize = 12f
        title.gravity = Gravity.CENTER
        title.maxLines = 2
        title.setLines(2)
        return title
    }

    private fun createIcon(image: String): ImageView {
        val icon = ImageUnify(itemView.context)
        val layoutParam =
            LinearLayout.LayoutParams(24.dpToPx(displayMetrics), 24.dpToPx(displayMetrics))
        layoutParam.topMargin = 8.dpToPx(displayMetrics)
        icon.layoutParams = layoutParam
        icon.loadImage(image)
        return icon
    }

    private fun Divider(width: Int = 8): View {
        val view = View(itemView.context)
        val layoutParam = LinearLayout.LayoutParams(
            width.dpToPx(displayMetrics),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParam
        return view
    }
}
