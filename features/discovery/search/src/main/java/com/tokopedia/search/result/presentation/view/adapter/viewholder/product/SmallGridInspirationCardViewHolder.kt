package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationCardOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import kotlinx.android.synthetic.main.search_result_product_curated_inspiration_card_layout.view.*
import kotlinx.android.synthetic.main.search_result_product_inspiration_card_layout.view.*
import kotlinx.android.synthetic.main.search_result_product_small_grid_inspiration_card_layout.view.*

class SmallGridInspirationCardViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
) : AbstractViewHolder<InspirationCardViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_small_grid_inspiration_card_layout
    }

    override fun bind(element: InspirationCardViewModel) {
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
            itemView.smallGridCardViewInspirationCard?.inspirationCard?.visibility = View.GONE
            itemView.smallGridCardViewInspirationCard?.inspirationCardCurated?.visibility = View.VISIBLE
        } else {
            itemView.smallGridCardViewInspirationCard?.inspirationCard?.visibility = View.VISIBLE
            itemView.smallGridCardViewInspirationCard?.inspirationCardCurated?.visibility = View.GONE
        }
    }

    private fun setCuratedLayout(element: InspirationCardViewModel) {
        val option = element.options.firstOrNull() ?: return

        bindCuratedIcon(option)
        bindCuratedTitle(option)
        bindCuratedListener(option)
    }

    private fun bindCuratedIcon(element: InspirationCardOptionViewModel) {
        itemView.smallGridCardViewInspirationCard?.inspirationCardCuratedIcon?.shouldShowWithAction(element.img.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(itemView.smallGridCardViewInspirationCard?.inspirationCardCuratedIcon, element.img)
        }
    }

    private fun bindCuratedTitle(element: InspirationCardOptionViewModel) {
        itemView.smallGridCardViewInspirationCard?.inspirationCardCuratedTitle?.shouldShowWithAction(element.text.isNotEmpty()) {
            itemView.smallGridCardViewInspirationCard?.inspirationCardCuratedTitle?.text = element.text
        }
    }

    private fun bindCuratedListener(element: InspirationCardOptionViewModel) {
        itemView.smallGridCardViewInspirationCard?.inspirationCardCuratedButton?.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(element)
        }
    }

    private fun setDefaultLayout(element: InspirationCardViewModel) {
        bindTitle(element)
        bindContent(element)
    }

    private fun bindTitle(element: InspirationCardViewModel) {
        itemView.smallGridCardViewInspirationCard?.inspirationCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.smallGridCardViewInspirationCard?.inspirationCardTitle?.text = element.title
        }
    }

    private fun bindContent(element: InspirationCardViewModel) {
        val spacingItemDecoration = ChipSpacingItemDecoration(
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8)
        )

        itemView.smallGridCardViewInspirationCard?.recyclerViewInspirationCardOptionList?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.options)
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun createAdapter(
            inspirationCarouselProductList: List<InspirationCardOptionViewModel>
    ): RecyclerView.Adapter<InspirationCardOptionChipViewHolder> {
        val inspirationCardOptionAdapter = InspirationCardOptionAdapter(inspirationCardListener)
        inspirationCardOptionAdapter.setItemList(inspirationCarouselProductList)

        return inspirationCardOptionAdapter
    }

}