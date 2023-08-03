package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessKeywordBinding
import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.changeview.ViewType
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory.InspirationKeywordsTypeFactoryImpl
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.INDEX_IMPRESSION_KEYWORD_TO_BE_TRACK
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.InspirationKeywordGridLayoutManager
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.KeywordItemDecoration
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isBigGridView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isListView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isSmallGridView
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class InspirationKeywordViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val changeViewListener: ChangeViewListener
) : AbstractViewHolder<InspirationKeywordCardView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_semless_keyword
        private const val TWO_COLUMN = 2
        private const val ONE_COLUMN = 1
    }

    private var binding: SearchInspirationSemlessKeywordBinding? by viewBinding()

    override fun bind(element: InspirationKeywordCardView?) {
        if (binding == null || element == null) return

        initCardView()
        binding?.searchSeamlessInspirationTitle?.text = element.title
        element.optionsItems.getItemOptionsOn(INDEX_IMPRESSION_KEYWORD_TO_BE_TRACK).doImpressedTracker()
        val viewType = changeViewListener.viewType
        initRecyclerView(element, viewType)
    }

    private fun initCardView() {
        val cardView = binding?.cardViewInspirationKeywordOptions
        cardView?.cardType = CardUnify2.TYPE_SHADOW
        cardView?.useCompatPadding = true
        cardView?.setMargin(0, 0, 0, 0)
    }

    private fun InspirationKeywordDataView.doImpressedTracker() {
        binding?.cardViewInspirationKeywordOptions?.addOnImpressionListener(this) {
            inspirationKeywordListener.onInspirationKeywordImpressed(this)
        }
    }

    private fun initRecyclerView(
        inspirationKeywords:  InspirationKeywordCardView,
        viewType: ViewType
    ) {
        binding?.inspirationKeywordOptionList?.let {
            val isNeedToShowNoImageCard = inspirationKeywords.isOneOrMoreIsEmptyImage
            val columnQty = generateColumnQty(inspirationKeywords.optionsItems.size, isNeedToShowNoImageCard)
            it.layoutManager = createLayoutManager(
                context = it.context,
                columnQty = columnQty,
                viewType
            )
            it.adapter =
                InspirationKeywordAdapter(
                    createInspirationKeywordTypeFactory(columnQty, isNeedToShowNoImageCard)
                ).apply {
                    setList(inspirationKeywords.optionsItems)
                }
            it.setItemDecoration(
                spacing = it.context.getSpacingResource(),
                spanQty = columnQty
            )
        }
    }

    private fun createInspirationKeywordTypeFactory(columnQty: Int, isNoImageCard: Boolean) =
        InspirationKeywordsTypeFactoryImpl(
            inspirationKeywordListener,
            changeViewListener,
            columnQty,
            isNoImageCard
        )

    private fun RecyclerView.setItemDecoration(spacing: Int, spanQty: Int) {
        this.addRemoveItemDecoration(KeywordItemDecoration(spacing, spanQty))
    }

    private fun RecyclerView.addRemoveItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        val hasNoItemDecoration = itemDecorationCount == 0
        if (hasNoItemDecoration) {
            addItemDecorationIfNotExists(itemDecoration)
        } else {
            removeItemDecorationAt(0)
            addItemDecorationIfNotExists(itemDecoration)
        }
    }

    private fun Context.getSpacingResource(): Int =
        this
            .resources
            ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
            ?: 0

    private fun createLayoutManager(
        context: Context,
        columnQty: Int,
        viewType: ViewType
    ): RecyclerView.LayoutManager {
        return when (viewType) {
            ViewType.LIST, ViewType.BIG_GRID ->
                createGridLayoutManager(context, columnQty)

            else ->
                createLinearLayoutManager()
        }
    }

    private fun createLinearLayoutManager() =
        LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)

    private fun createGridLayoutManager(context: Context, sizeList: Int) =
        InspirationKeywordGridLayoutManager(context, sizeList)

    private fun generateColumnQty(sizeList: Int, isNoImageCard: Boolean) =
        when {
            changeViewListener.isBigGridView() || isBigGridOrListAndNoImage(
                isNoImageCard
            ) -> TWO_COLUMN
            changeViewListener.isSmallGridView() -> ONE_COLUMN
            changeViewListener.isListView() -> sizeList
            else -> 1
        }

    private fun isBigGridOrListAndNoImage(isNoImage: Boolean) =
        changeViewListener.isBigGridView() && isNoImage || isNoImage && changeViewListener.isListView()

    private fun List<InspirationKeywordDataView>.getItemOptionsOn(position: Int) =
        this[position]
}
