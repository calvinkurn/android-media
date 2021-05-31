package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Rect
import android.os.Build
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationCardOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.search_result_product_big_grid_curated_inspiration_card_layout.view.*
import kotlinx.android.synthetic.main.search_result_product_big_grid_inspiration_card_layout.view.*
import kotlinx.android.synthetic.main.search_result_product_inspiration_card_layout.view.*

class BigGridInspirationCardViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
) : AbstractViewHolder<InspirationCardDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_big_grid_inspiration_card_layout
        private const val SPAN_COUNT = 2
    }

    override fun bind(element: InspirationCardDataView) {
        val isCurated = element.type == SearchConstant.InspirationCard.TYPE_CURATED
        setBaseLayout(isCurated)
        if (isCurated) {
            setCuratedLayout(element)
        } else {
            setDefaultLayout(element)
        }
    }

    private fun setBaseLayout(isCurated: Boolean) {
        if (isCurated) {
            itemView.bigGridCardViewInspirationCard?.inspirationCard?.visibility = View.GONE
            itemView.bigGridCardViewInspirationCard?.inspirationCardCurated?.visibility = View.VISIBLE
        } else {
            itemView.bigGridCardViewInspirationCard?.inspirationCard?.visibility = View.VISIBLE
            itemView.bigGridCardViewInspirationCard?.inspirationCardCurated?.visibility = View.GONE
        }
    }

    private fun setCuratedLayout(element: InspirationCardDataView) {
        val option = element.optionData.firstOrNull() ?: return

        bindCuratedBackground()
        bindCuratedIcon(option)
        bindCuratedTitle(option)
        bindCuratedListener(option)
    }

    private fun bindCuratedBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            itemView.inspirationCardCuratedBackground?.setBackgroundResource(R.drawable.search_background_layer_big_grid_curated_cards)
            itemView.inspirationCardCuratedBackground?.visibility = View.VISIBLE
        }
        else itemView.inspirationCardCuratedBackground?.visibility = View.GONE
    }

    private fun bindCuratedIcon(element: InspirationCardOptionDataView) {
        itemView.bigGridCardViewInspirationCard?.inspirationCardCuratedIcon?.shouldShowWithAction(element.img.isNotEmpty()) {
            itemView.bigGridCardViewInspirationCard?.inspirationCardCuratedIcon?.loadImageFitCenter(element.img)
        }
    }

    private fun bindCuratedTitle(element: InspirationCardOptionDataView) {
        itemView.bigGridCardViewInspirationCard?.inspirationCardCuratedTitle?.shouldShowWithAction(element.text.isNotEmpty()) {
            itemView.bigGridCardViewInspirationCard?.inspirationCardCuratedTitle?.text = element.text
        }
    }

    private fun bindCuratedListener(element: InspirationCardOptionDataView) {
        itemView.bigGridCardViewInspirationCard?.inspirationCardCuratedButton?.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(element)
        }
    }

    private fun setDefaultLayout(element: InspirationCardDataView) {
        bindTitle(element)
        bindContent(element)
    }

    private fun bindTitle(element: InspirationCardDataView) {
        itemView.bigGridCardViewInspirationCard?.inspirationCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.bigGridCardViewInspirationCard?.inspirationCardTitle?.text = element.title
        }
    }

    private fun bindContent(element: InspirationCardDataView) {
        itemView.bigGridCardViewInspirationCard?.recyclerViewInspirationCardOptionList?.let {
            it.layoutManager = createLayoutManager(element)
            it.adapter = createAdapter(element.optionData)
            it.addItemDecorationIfNotExists(createItemDecoration(element))
        }
    }

    private fun createLayoutManager(element: InspirationCardDataView): RecyclerView.LayoutManager {
        return if (!element.isRelated()) {
            ChipsLayoutManager.newBuilder(itemView.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
        } else {
            GridLayoutManager(itemView.context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun createAdapter(
            inspirationCarouselProductListData: List<InspirationCardOptionDataView>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val inspirationCardOptionAdapter = InspirationCardOptionAdapter(inspirationCardListener, SPAN_COUNT)
        inspirationCardOptionAdapter.setItemList(inspirationCarouselProductListData)

        return inspirationCardOptionAdapter
    }

    private fun createItemDecoration(element: InspirationCardDataView): RecyclerView.ItemDecoration {
        val spacing = 8.toPx()

        return if (!element.isRelated()) ChipSpacingItemDecoration(spacing, spacing)
        else RelatedBigGridItemDecoration(spacing)
    }

    private class RelatedBigGridItemDecoration(
            private val horizontalSpacing: Int,
    ): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val childPosition = parent.getChildAdapterPosition(view)
            val isLeftPosition = childPosition % SPAN_COUNT == 0
            val isRightPosition = childPosition % SPAN_COUNT == 1

            outRect.left = if (isRightPosition) this.horizontalSpacing else 0
            outRect.right = if (isLeftPosition) this.horizontalSpacing else 0
        }
    }
}