package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_CLEAR
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ComparisonViewHolder(
    itemView: View,
    private val comparisonItemListener: ComparisonItemListener? = null
) : AbstractViewHolder<ComparisonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_comparison
    }

    private val binding by viewBinding<WidgetItemComparisonBinding>()

    private fun WidgetItemComparisonBinding.setupComparisonListItem(contents: List<ComparisonUiModel.ComparisonContent>) {
        rvComparisonItems.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ComparisonItemAdapter(
                contents,
                binding?.layoutComparison?.root?.measuredWidth.orZero(),
                comparisonItemListener
            )
        }
    }

    override fun bind(element: ComparisonUiModel) {
        val comparisonItems = element.content.subList(Int.ONE, element.content.size)
        val comparedItem = element.content.firstOrNull()
        val colorGray = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN500)
        binding?.layoutComparison?.apply {
            iuProduct.loadImage(comparedItem?.imageUrl.orEmpty())
            cardProductAction.cardType = TYPE_CLEAR
            iconProductAction.setImage(IconUnify.PUSH_PIN_FILLED, colorGray)
            root.addOneTimeGlobalLayoutListener {
                binding?.setupComparisonListItem(comparisonItems)
            }
        }
    }

    class ComparisonItemAdapter(
        private var items: List<ComparisonUiModel.ComparisonContent> = listOf(),
        private var itemWidth: Int,
        private val comparisonItemListener: ComparisonItemListener? = null
    ) : RecyclerView.Adapter<ComparisonItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonItemViewHolder {
            val rootView = ComparisonItemViewHolder.createRootView(parent)
            return ComparisonItemViewHolder(rootView, comparisonItemListener)
        }

        override fun onBindViewHolder(holder: ComparisonItemViewHolder, position: Int) {
            holder.bind(items[position], itemWidth)
        }

        override fun getItemCount() = items.size
    }

    class ComparisonItemViewHolder(
        itemView: View,
        private val comparisonItemListener: ComparisonItemListener? = null
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
                iuProduct.loadImage(item.imageUrl)
                root.layoutParams.width = itemWidth
                tfProductName.text = item.productTitle
                tfProductPrice.text = item.price
            }
        }
    }

    interface ComparisonItemListener {
        fun onComparisonSwitchButtonClicked(position: Int)
    }
}
