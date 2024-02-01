package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.comparison.ComparisonItemAdapter
import com.tokopedia.catalogcommon.viewholder.comparison.ComparisonSpecItemAdapter
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.ceil

class ComparisonViewHolder(
    itemView: View,
    private val comparisonItemListener: ComparisonItemListener? = null,
    private val isDisplayingTopSpec: Boolean = true
) : AbstractViewHolder<ComparisonUiModel>(itemView) {

    interface ComparisonItemListener {
        fun onComparisonSwitchButtonClicked(
            items: List<ComparisonUiModel.ComparisonContent>
        )

        fun onComparisonSeeMoreButtonClicked(items: List<ComparisonUiModel.ComparisonContent>)
        fun onComparisonProductClick(id: String)

        fun onComparisonImpression(id: String, widgetName: String)
        fun onComparisonScrolled(dx: Int, dy: Int, scrollProgress: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_comparison
        private const val DEFAULT_LINE_COUNT = 1
        private const val DEFAULT_CHAR_WIDTH = 15
        private const val DEFAULT_TITLE_CHAR_WIDTH = 20
        private const val TOP_SPEC_MARGIN = 16
        private const val WIDE_WIDTH_ITEM_COUNT = 2
        private const val NORMAL_WIDTH_DP_VALUE = 148
        private const val MAX_PRODUCT_TITLE_LINES = 2
        private const val INTRO_ANIMATION_START = 1000L
        private const val INTRO_ANIMATION_END = 3000L
    }

    private val binding by viewBinding<WidgetItemComparisonBinding>()
    private var comparisonContents: List<ComparisonUiModel.ComparisonContent> = emptyList()
    private var scrollProgress = 0
    private var introAnimationPlayed = false

    init {
        binding?.btnSeeMore?.setOnClickListener {
            comparisonItemListener?.onComparisonSeeMoreButtonClicked(comparisonContents)
        }
        binding?.tfCatalogAction?.setOnClickListener {
            comparisonItemListener?.onComparisonSwitchButtonClicked(comparisonContents)
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scrollProgress += dx
                    comparisonItemListener?.onComparisonScrolled(dx, dy, scrollProgress)
                }
            })
        }
    }

    private fun WidgetItemComparisonBinding.setupLayoutComparison(
        comparedItem: ComparisonUiModel.ComparisonContent?,
        comparisonItems: List<ComparisonUiModel.ComparisonContent>
    ) {
        val specs =
            if (isDisplayingTopSpec) comparedItem?.topComparisonSpecs else comparedItem?.comparisonSpecs
        layoutComparison.apply {
            tfProductName.text = comparedItem?.productTitle.orEmpty()
            tfProductPrice.text = comparedItem?.price.orEmpty()
            iuProduct.loadImage(comparedItem?.imageUrl.orEmpty())
            root.addOneTimeGlobalLayoutListener {
                val textAreaWidth: Double = tfProductPrice.measuredWidth.orZero().toDouble()
                configureRowsHeight(textAreaWidth, comparedItem, comparisonItems)
                rvSpecs.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                rvSpecs.adapter = ComparisonSpecItemAdapter(specs.orEmpty(), true)
                setupComparisonListItem(comparisonItems)
                playIntroAnimation(rvComparisonItems, comparisonItems.count().dec())
            }
        }
    }

    private fun playIntroAnimation(rvComparisonItems: RecyclerView, lastIndex: Int) {
        if (introAnimationPlayed || !isDisplayingTopSpec) return
        introAnimationPlayed = true
        rvComparisonItems.postDelayed({
            rvComparisonItems.smoothScrollToPosition(lastIndex)
        }, INTRO_ANIMATION_START)
        rvComparisonItems.postDelayed({
            rvComparisonItems.smoothScrollToPosition(Int.ZERO)
        }, INTRO_ANIMATION_END)
    }

    private fun configureRowsHeight(
        textAreaWidth: Double,
        comparedItem: ComparisonUiModel.ComparisonContent?,
        comparisonItems: List<ComparisonUiModel.ComparisonContent>
    ) {
        val specs =
            if (isDisplayingTopSpec) comparedItem?.topComparisonSpecs else comparedItem?.comparisonSpecs
        val rowsHeight = List(specs?.size.orZero()) { DEFAULT_LINE_COUNT }.toMutableList()
        var titleHeight = DEFAULT_LINE_COUNT

        // update list
        comparisonItems.forEach {
            val tempTitleHeight =
                ceil((it.productTitle.length * DEFAULT_TITLE_CHAR_WIDTH) / textAreaWidth).toInt()
            if (tempTitleHeight == MAX_PRODUCT_TITLE_LINES) titleHeight = tempTitleHeight
            if (isDisplayingTopSpec) {
                it.topComparisonSpecs.updateRowsHeight(rowsHeight, textAreaWidth)
            } else {
                it.comparisonSpecs.updateRowsHeight(rowsHeight, textAreaWidth)
            }
        }
        specs?.updateRowsHeight(rowsHeight, textAreaWidth)

        // apply list to object
        comparisonItems.forEach {
            it.titleHeight = titleHeight
            if (isDisplayingTopSpec) {
                it.topComparisonSpecs.applyRowsHeight(rowsHeight)
            } else {
                it.comparisonSpecs.applyRowsHeight(rowsHeight)
            }
        }
        specs?.applyRowsHeight(rowsHeight)

        // apply to comparison title
        binding?.layoutComparison?.tfProductName?.apply {
            maxLines = titleHeight
            minLines = titleHeight
        }
    }

    private fun List<ComparisonUiModel.ComparisonSpec>.updateRowsHeight(
        rowsHeight: MutableList<Int>,
        textAreaWidth: Double
    ) {
        forEachIndexed { index, comparisonSpec ->
            val lines = comparisonSpec.specValue.split("\n").sumOf { line ->
                ceil((line.length * DEFAULT_CHAR_WIDTH) / textAreaWidth).toInt()
            }
            if (rowsHeight.getOrNull(index) != null) {
                if (lines > rowsHeight[index]) rowsHeight[index] = lines
            }
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
            }
        }
    }

    private fun WidgetItemComparisonBinding.setupTopSpecs(element: ComparisonUiModel) {
        btnSeeMore.isVisible = isDisplayingTopSpec
        tfCatalogTitle.isVisible = isDisplayingTopSpec
        tfCatalogAction.isVisible = isDisplayingTopSpec
        if (!isDisplayingTopSpec) {
            layoutComparison.root.setBackgroundResource(Int.ZERO)
        } else {
            val margin = TOP_SPEC_MARGIN.dpToPx(itemView.resources.displayMetrics)
            layoutComparison.root.setMargin(margin, margin, Int.ZERO, Int.ZERO)
        }
    }

    private fun WidgetItemComparisonBinding.setupWidth(contentSize: Int) {
        val screenWidth = if (contentSize <= WIDE_WIDTH_ITEM_COUNT) {
            getScreenWidth() / WIDE_WIDTH_ITEM_COUNT
        } else {
            NORMAL_WIDTH_DP_VALUE.dpToPx(itemView.resources.displayMetrics)
        }
        guidelineComparison.setGuidelineBegin(screenWidth)
    }

    override fun bind(element: ComparisonUiModel) {
        val comparedId = element.content.slice(Int.ONE until element.content.size).joinToString(",") { it.id }
        comparisonItemListener?.onComparisonImpression(
            comparedId,
            element.widgetName
        )
        if (element.content.isEmpty()) return
        this.comparisonContents = element.content
        val comparisonItems = element.content.subList(Int.ONE, element.content.size)
        val comparedItem = element.content.firstOrNull()
        binding?.setupWidth(element.content.size)
        binding?.setupLayoutComparison(comparedItem, comparisonItems)
        binding?.setupColors(element)
        binding?.setupTopSpecs(element)
    }
}
