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
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductBigGridInspirationCardLayoutBinding
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationCardOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.LazyThreadSafetyMode.NONE

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

    private var bigGridInspirationCardBinding:
        SearchResultProductBigGridInspirationCardLayoutBinding? by viewBinding()

    private val inspirationCardBinding by lazy(NONE) {
        bigGridInspirationCardBinding?.bigGridInspirationCardLayout
    }

    private val inspirationCardCuratedBinding by lazy(NONE) {
        bigGridInspirationCardBinding?.bigGridCuratedInspirationCardLayout
    }

    override fun bind(element: InspirationCardDataView) {
        val isCurated = element.data.type == SearchConstant.InspirationCard.TYPE_CURATED
        setBaseLayout(isCurated)
        if (isCurated) {
            setCuratedLayout(element)
        } else {
            setDefaultLayout(element)
        }
    }

    override fun onViewRecycled() {
        inspirationCardCuratedBinding?.inspirationCardCuratedIcon?.clearImage()
    }

    private fun setBaseLayout(isCurated: Boolean) {
        if (isCurated) {
            inspirationCardBinding?.root?.visibility = View.GONE
            inspirationCardCuratedBinding?.root?.visibility = View.VISIBLE
        } else {
            inspirationCardBinding?.root?.visibility = View.VISIBLE
            inspirationCardCuratedBinding?.root?.visibility = View.GONE
        }
    }

    private fun setCuratedLayout(element: InspirationCardDataView) {
        val option = element.data.optionCardData.firstOrNull() ?: return

        bindCuratedBackground()
        bindCuratedIcon(option)
        bindCuratedTitle(option)
        bindCuratedListener(option)
    }

    private fun bindCuratedBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            inspirationCardCuratedBinding?.inspirationCardCuratedBackground?.let {
                it.setBackgroundResource(R.drawable.search_background_layer_big_grid_curated_cards)
                it.visibility = View.VISIBLE
            }
        } else inspirationCardCuratedBinding?.inspirationCardCuratedBackground?.visibility = View.GONE
    }

    private fun bindCuratedIcon(element: InspirationCardOptionDataView) {
        inspirationCardCuratedBinding?.inspirationCardCuratedIcon?.let {
            it.shouldShowWithAction(element.img.isNotEmpty()) {
                it.loadImageFitCenter(element.img)
            }
        }
    }

    private fun bindCuratedTitle(element: InspirationCardOptionDataView) {
        inspirationCardCuratedBinding?.inspirationCardCuratedTitle?.let {
            it.shouldShowWithAction(element.text.isNotEmpty()) {
                it.text = element.text
            }
        }
    }

    private fun bindCuratedListener(element: InspirationCardOptionDataView) {
        inspirationCardCuratedBinding?.inspirationCardCuratedButton?.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(element)
        }
    }

    private fun setDefaultLayout(element: InspirationCardDataView) {
        bindTitle(element)
        bindContent(element)
    }

    private fun bindTitle(element: InspirationCardDataView) {
        inspirationCardBinding?.inspirationCardTitle?.let {
            it.shouldShowWithAction(element.data.title.isNotEmpty()) {
                it.text = element.data.title
            }
        }
    }

    private fun bindContent(element: InspirationCardDataView) {
        inspirationCardBinding?.recyclerViewInspirationCardOptionList?.let {
            it.layoutManager = createLayoutManager(element)
            it.adapter = createAdapter(element.data.optionCardData)
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
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val childPosition = parent.getChildAdapterPosition(view)
            val isLeftPosition = childPosition % SPAN_COUNT == 0
            val isRightPosition = childPosition % SPAN_COUNT == 1

            outRect.left = if (isRightPosition) this.horizontalSpacing else 0
            outRect.right = if (isLeftPosition) this.horizontalSpacing else 0
        }
    }
}