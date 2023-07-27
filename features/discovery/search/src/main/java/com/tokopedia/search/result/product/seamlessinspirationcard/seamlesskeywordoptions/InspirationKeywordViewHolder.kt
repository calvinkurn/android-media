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
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.INDEX_IMPRESISON_KEYWORD_TO_BE_TRACK
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.InspirationKeywordGridLayoutManager
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.KeywordItemDecoration
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
        private const val BIG_GRID_COLUMN = 2
        private const val SMALL_GRID_COLUMN = 1
        val QTY_EXAMPLE_DATA = 3
    }

    private var binding: SearchInspirationSemlessKeywordBinding? by viewBinding()

    override fun bind(element: InspirationKeywordCardView?) {
        if (binding == null || element == null) return

        initCardView()
        val optionItem = element.optionsItems.take(QTY_EXAMPLE_DATA)
        optionItem.getItemOptionsOn(INDEX_IMPRESISON_KEYWORD_TO_BE_TRACK).doImpressedTracker()
        val viewType = changeViewListener.viewType
        initRecyclerView(optionItem, viewType)
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

    private fun initRecyclerView(inspirationKeywords: List<InspirationKeywordDataView>, viewType: ViewType) {
        binding?.inspirationKeywordOptionList?.let {
            val columnQty = generateColumnQty(inspirationKeywords.size)
            it.layoutManager = createLayoutManager(
                context = it.context,
                columnQty = columnQty,
                viewType
            )
            it.adapter =
                InspirationKeywordAdapter(
                    createInspirationKeywordTypeFactory(columnQty)
                ).apply {
                    setList(inspirationKeywords)
                }
            it.setItemDecoration(
                spacing = it.context.getSpacingResource(),
                spanQty = columnQty
            )
        }
    }

    private fun createInspirationKeywordTypeFactory(columnQty: Int) = InspirationKeywordsTypeFactoryImpl(
        inspirationKeywordListener,
        changeViewListener,
        columnQty,
        false
    )

    private fun RecyclerView.setItemDecoration(spacing: Int, spanQty: Int) {
        this.addRemoveItemDecoration(KeywordItemDecoration(spacing, spanQty))
    }

    private fun RecyclerView.addRemoveItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        val hasNoItemDecoration = itemDecorationCount == 0
        if (hasNoItemDecoration) {
            addItemDecoration(itemDecoration)
        } else {
            removeItemDecorationAt(0)
            addItemDecoration(itemDecoration)
        }
    }

    private fun Context.getSpacingResource(): Int =
        this
            .resources
            ?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
            ?: 0

    private fun createLayoutManager(context: Context, columnQty: Int, viewType: ViewType): RecyclerView.LayoutManager {
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

    private fun generateColumnQty(sizeList: Int) =
        when (changeViewListener.viewType) {
            ViewType.BIG_GRID -> BIG_GRID_COLUMN
            ViewType.SMALL_GRID -> SMALL_GRID_COLUMN
            ViewType.LIST -> sizeList
        }

    private fun List<InspirationKeywordDataView>.getItemOptionsOn(position: Int) =
        this[position]
}
