package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.CarouselItemSampleLayoutBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemDoubleBannerImageContentBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonViewHolder(itemView: View) : AbstractViewHolder<ComparisonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_comparison
    }

    private val binding by viewBinding<WidgetItemComparisonBinding>()

    private fun WidgetItemComparisonBinding.setupComparisonListItem() {
        rvComparisonItems.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ComparisonAdapter(
                listOf(
                    Pair("https://placekitten.com/200/200", "https://placekitten.com/200/200"),
                    Pair("https://placekitten.com/200/200", "https://placekitten.com/200/200"),
                ),
                binding?.layoutComparison?.root?.measuredWidth.orZero()
            )
        }
    }

    override fun bind(element: ComparisonUiModel) {
        binding?.apply {
            layoutComparison.contentRight.loadImage("https://placekitten.com/200/200")
            layoutComparison.root.addOneTimeGlobalLayoutListener {
                binding?.setupComparisonListItem()
            }
        }
    }

    class ComparisonAdapter(
        private var items: List<Pair<String, String>> = listOf(),
        private var itemWidth: Int = 0
    ) : RecyclerView.Adapter<ComparisonViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonViewHolder {
            val rootView = ComparisonViewHolder.createRootView(parent)
            return ComparisonViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: ComparisonViewHolder, position: Int) {
            holder.bind(items[position], itemWidth)
        }

        override fun getItemCount() = items.size
    }

    class ComparisonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_item_comparison_content, parent, false)
        }

        private val binding: WidgetItemComparisonContentBinding? by viewBinding()

        fun bind(item: Pair<String, String>, itemWidth: Int = 0) {
            binding?.apply {
                contentRight.loadImage(item.first)
                root.layoutParams.width = itemWidth
                tfProductName.text = item.second
            }
        }
    }
}
