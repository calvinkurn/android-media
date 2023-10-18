package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.comparison.ComparisonItemAdapter
import com.tokopedia.catalogcommon.viewholder.comparison.ComparisonSpecItemAdapter
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_CLEAR
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.ceil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ComparisonViewHolder(
    itemView: View,
    private val comparisonItemListener: ComparisonItemListener? = null,
    private val isDisplayingTopSpec: Boolean = true
) : AbstractViewHolder<ComparisonUiModel>(itemView) {

    interface ComparisonItemListener {
        fun onComparisonSwitchButtonClicked(position: Int)
        fun onComparisonSeeMoreButtonClicked()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_comparison
        private const val DEFAULT_LINE_COUNT = 1
        private const val DEFAULT_CHAR_WIDTH = 15
    }

    private val binding by viewBinding<WidgetItemComparisonBinding>()

    init {
        binding?.btnSeeMore?.setOnClickListener {
            comparisonItemListener?.onComparisonSeeMoreButtonClicked()
        }
    }

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
                val textAreaWidth: Double = tfProductPrice.measuredWidth.orZero().toDouble()
                configureRowsHeight(textAreaWidth, comparedItem, comparisonItems)
                rvSpecs.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                rvSpecs.adapter = ComparisonSpecItemAdapter(specs.orEmpty(), true)
                setupComparisonListItem(comparisonItems)
            }
        }
    }

    private fun configureRowsHeight(
        textAreaWidth: Double,
        comparedItem: ComparisonUiModel.ComparisonContent?,
        comparisonItems: List<ComparisonUiModel.ComparisonContent>
    ) {
        val specs = if (isDisplayingTopSpec) comparedItem?.topComparisonSpecs else comparedItem?.comparisonSpecs
        val rowsHeight = List(specs?.size.orZero()) { DEFAULT_LINE_COUNT }.toMutableList()

        // update list
        comparisonItems.forEach {
            if (isDisplayingTopSpec)
                it.topComparisonSpecs.updateRowsHeight(rowsHeight, textAreaWidth)
            else
                it.comparisonSpecs.updateRowsHeight(rowsHeight, textAreaWidth)
        }
        specs?.updateRowsHeight(rowsHeight, textAreaWidth)

        // apply list to object
        comparisonItems.forEach {
            if (isDisplayingTopSpec)
                it.topComparisonSpecs.applyRowsHeight(rowsHeight)
            else
                it.comparisonSpecs.applyRowsHeight(rowsHeight)
        }
        specs?.applyRowsHeight(rowsHeight)
    }

    private fun List<ComparisonUiModel.ComparisonSpec>.updateRowsHeight(
        rowsHeight: MutableList<Int>,
        textAreaWidth: Double
    ) {
        forEachIndexed { index, comparisonSpec ->
            val lines = ceil((comparisonSpec.specValue.length * DEFAULT_CHAR_WIDTH)/ textAreaWidth).toInt()
            if (rowsHeight.getOrNull(index) != null)
                if (lines > rowsHeight[index]) rowsHeight[index] = lines
        }
    }

    private fun List<ComparisonUiModel.ComparisonSpec>.applyRowsHeight(rowsHeight: MutableList<Int>) {
        forEachIndexed { index, comparisonSpec ->
            comparisonSpec.specHeight = rowsHeight.getOrNull(index) ?: DEFAULT_LINE_COUNT
        }
    }

    private fun WidgetItemComparisonBinding.setupColors(element: ComparisonUiModel) {
        if (!isDisplayingTopSpec) return
        if (element.darkMode) {
            btnSeeMore.applyColorMode(ColorMode.DARK_MODE)
        } else {
            btnSeeMore.applyColorMode(ColorMode.LIGHT_MODE)
        }
        tfCatalogTitle.setTextColor(element.widgetTextColor ?: return)
        layoutComparison.apply {
            tfProductName.setTextColor(element.widgetTextColor ?: return)
            tfProductPrice.setTextColor(element.widgetTextColor ?: return)
            element.content.forEach { comparisonContent ->
                comparisonContent.productTextColor = element.widgetTextColor
                comparisonContent.topComparisonSpecs.forEach { comparisonSpec ->
                    comparisonSpec.specTextColor = element.widgetTextColor
                }
            }
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
}
