package com.tokopedia.catalogcommon.viewholder.comparison

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonItemViewHolder(
    itemView: View,
    private val isDisplayingTopSpec: Boolean,
    private val comparisonItemListener: ComparisonViewHolder.ComparisonItemListener? = null
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_comparison_content, parent, false)
    }

    private val binding: WidgetItemComparisonContentBinding? by viewBinding()

    fun bind(item: ComparisonUiModel.ComparisonContent, itemWidth: Int, isLastPosition: Boolean) {
        binding?.apply {
            clProduct.setOnClickListener {
                comparisonItemListener?.onComparisonProductClick(item.id)
            }
            val specs = if (isDisplayingTopSpec) item.topComparisonSpecs else item.comparisonSpecs
            iuProduct.loadImage(item.imageUrl)
            root.layoutParams.width = itemWidth
            tfProductName.text = item.productTitle
            tfProductPrice.text = item.price
            rvSpecs.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            rvSpecs.adapter = ComparisonSpecItemAdapter(specs)
            tfProductName.maxLines = item.titleHeight
            tfProductName.minLines = item.titleHeight

            if (isDisplayingTopSpec) this.drawOutsideBorder(isLastPosition)
            else root.setBackgroundResource(Int.ZERO)

            tfProductName.setTextColor(item.productTextColor ?: return)
            tfProductPrice.setTextColor(item.productTextColor ?: return)
        }
    }

    private fun WidgetItemComparisonContentBinding.drawOutsideBorder(isLastPosition: Boolean) {
        root.background = MethodChecker.getDrawable(
            root.context,
            if (!isLastPosition) R.drawable.bg_comparison_table_center
            else R.drawable.bg_comparison_table_right
        )
    }
}
