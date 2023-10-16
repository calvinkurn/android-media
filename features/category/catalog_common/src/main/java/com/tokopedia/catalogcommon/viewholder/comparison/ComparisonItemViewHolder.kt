package com.tokopedia.catalogcommon.viewholder.comparison

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
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

    init {
        binding?.cardProductAction?.setOnClickListener {
            comparisonItemListener?.onComparisonSwitchButtonClicked(bindingAdapterPosition)
        }
    }

    fun bind(item: ComparisonUiModel.ComparisonContent, itemWidth: Int) {
        binding?.apply {
            val specs = if (isDisplayingTopSpec) item.topComparisonSpecs else item.comparisonSpecs
            iuProduct.loadImage(item.imageUrl)
            root.layoutParams.width = itemWidth
            tfProductName.text = item.productTitle
            tfProductPrice.text = item.price
            rvSpecs.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            rvSpecs.adapter = ComparisonSpecItemAdapter(specs)
        }
    }
}
