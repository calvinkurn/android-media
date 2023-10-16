package com.tokopedia.catalogcommon.viewholder

import android.text.TextUtils
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
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentSpecBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_CLEAR
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ComparisonViewHolder(
    itemView: View,
    private val comparisonItemListener: ComparisonItemListener? = null,
    private val isDisplayingTopSpec: Boolean = true
) : AbstractViewHolder<ComparisonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_comparison
    }

    private val binding by viewBinding<WidgetItemComparisonBinding>()

    private fun WidgetItemComparisonBinding.setupComparisonListItem(
        contents: List<ComparisonUiModel.ComparisonContent>
    ) {
        rvComparisonItems.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ComparisonItemAdapter(
                contents,
                binding?.layoutComparison?.root?.measuredWidth.orZero(),
                isDisplayingTopSpec,
                comparisonItemListener
            )
        }
    }

    private fun WidgetItemComparisonBinding.setupLayoutComparison(
        comparedItem: ComparisonUiModel.ComparisonContent?,
        comparisonItems: List<ComparisonUiModel.ComparisonContent>
    ) {
        val colorGray = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN500)
        val specs = if (isDisplayingTopSpec) comparedItem?.topComparisonSpecs else comparedItem?.comparisonSpecs
        layoutComparison.apply {
            tfProductName.text = comparedItem?.productTitle.orEmpty()
            tfProductPrice.text = comparedItem?.price.orEmpty()
            iuProduct.loadImage(comparedItem?.imageUrl.orEmpty())
            cardProductAction.cardType = TYPE_CLEAR
            iconProductAction.setImage(IconUnify.PUSH_PIN_FILLED, colorGray)
            root.addOneTimeGlobalLayoutListener {
                val heightTemp = List(specs?.size.orZero()) { 1 }.toMutableList()
                val textAreaWidth: Double = tfProductPrice.measuredWidth.orZero().toDouble()
                comparisonItems.forEach {
                    it.topComparisonSpecs.forEachIndexed { index, comparisonSpec ->
                        val lines = ceil((comparisonSpec.specValue.length * 15)/ textAreaWidth).toInt()
                        if (lines > heightTemp[index]) heightTemp[index] = lines
                    }
                    it.comparisonSpecs.forEachIndexed { index, comparisonSpec ->
                        val lines = ceil((comparisonSpec.specValue.length * 15)/ textAreaWidth).toInt()
                        if (lines > heightTemp[index]) heightTemp[index] = lines
                    }
                }
                specs?.forEachIndexed { index, comparisonSpec ->
                    val lines = ceil((comparisonSpec.specValue.length * 15)/ textAreaWidth).toInt()
                    if (lines > heightTemp[index]) heightTemp[index] = lines
                }
                comparisonItems.forEach {
                    it.topComparisonSpecs.forEachIndexed { index, comparisonSpec ->
                        comparisonSpec.specHeight = heightTemp[index]
                    }
                    it.comparisonSpecs.forEachIndexed { index, comparisonSpec ->
                        comparisonSpec.specHeight = heightTemp[index]
                    }
                }
                specs?.forEachIndexed { index, comparisonSpec ->
                    comparisonSpec.specHeight = heightTemp[index]
                }
                rvSpecs.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                rvSpecs.adapter = ComparisonSpecItemAdapter(specs.orEmpty(), true)
                setupComparisonListItem(comparisonItems)
            }
        }
    }

    private fun WidgetItemComparisonBinding.setupColors(element: ComparisonUiModel) {
        if (element.darkMode) {
            btnSeeMore.applyColorMode(ColorMode.DARK_MODE)
        } else {
            btnSeeMore.applyColorMode(ColorMode.LIGHT_MODE)
        }
    }

    private fun WidgetItemComparisonBinding.setupTopSpecs(element: ComparisonUiModel) {
        btnSeeMore.isVisible = isDisplayingTopSpec
        tfCatalogTitle.isVisible = isDisplayingTopSpec
    }

    override fun bind(element: ComparisonUiModel) {
        val comparisonItems = element.content.subList(Int.ONE, element.content.size)
        val comparedItem = element.content.firstOrNull()
        binding?.setupLayoutComparison(comparedItem, comparisonItems)
        binding?.setupColors(element)
        binding?.setupTopSpecs(element)
    }

    class ComparisonItemAdapter(
        private val items: List<ComparisonUiModel.ComparisonContent> = listOf(),
        private val itemWidth: Int,
        private val isDisplayingTopSpec: Boolean = false,
        private val comparisonItemListener: ComparisonItemListener? = null
    ) : RecyclerView.Adapter<ComparisonItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonItemViewHolder {
            val rootView = ComparisonItemViewHolder.createRootView(parent)
            return ComparisonItemViewHolder(rootView, isDisplayingTopSpec, comparisonItemListener)
        }

        override fun onBindViewHolder(holder: ComparisonItemViewHolder, position: Int) {
            holder.bind(items[position], itemWidth)
        }

        override fun getItemCount() = items.size
    }

    class ComparisonItemViewHolder(
        itemView: View,
        private val isDisplayingTopSpec: Boolean,
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

    class ComparisonSpecItemAdapter(
        private val items: List<ComparisonUiModel.ComparisonSpec> = listOf(),
        private val isComparedItem: Boolean = false
    ) : RecyclerView.Adapter<ComparisonSpecItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonSpecItemViewHolder {
            val rootView = ComparisonSpecItemViewHolder.createRootView(parent)
            return ComparisonSpecItemViewHolder(rootView, isComparedItem)
        }

        override fun onBindViewHolder(holder: ComparisonSpecItemViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }

    class ComparisonSpecItemViewHolder(
        itemView: View,
        isComparedItem: Boolean
    ) : RecyclerView.ViewHolder(itemView) {
        companion object {
            private val DIVIDER_MARGIN = 8
            fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_item_comparison_content_spec, parent, false)
        }

        private val binding: WidgetItemComparisonContentSpecBinding? by viewBinding()

        init {
            if (isComparedItem) {
                binding?.apply {
                    val margin = DIVIDER_MARGIN.dpToPx(itemView.resources.displayMetrics)
                    divSpecCategory.setMargin(margin, Int.ZERO, Int.ZERO, Int.ZERO)
                    divSpecValue.setMargin(margin, Int.ZERO, Int.ZERO, Int.ZERO)
                }
            }
        }

        fun bind(item: ComparisonUiModel.ComparisonSpec) {
            itemView.post {
                binding?.apply {
                    tfSpecValue.ellipsize = TextUtils.TruncateAt.END
                    tfSpecValue.maxLines = item.specHeight
                    tfSpecValue.minLines = item.specHeight
                    tfSpecCategoryTitle.text = item.specCategoryTitle
                    tfSpecTitle.text = item.specTitle
                    tfSpecValue.text = item.specValue

                    tfSpecCategoryTitle.isVisible = item.isSpecCategoryTitle
                    divSpecCategory.isVisible = item.isSpecCategoryTitle
                    tfSpecTitle.isVisible = !item.isSpecCategoryTitle
                    tfSpecValue.isVisible = !item.isSpecCategoryTitle
                    divSpecValue.isVisible = !item.isSpecCategoryTitle
                }
            }
        }
    }

    interface ComparisonItemListener {
        fun onComparisonSwitchButtonClicked(position: Int)
    }
}
