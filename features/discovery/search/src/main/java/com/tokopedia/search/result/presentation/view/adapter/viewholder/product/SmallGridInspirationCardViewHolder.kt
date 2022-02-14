package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.os.Build
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductInspirationCardLayoutBinding
import com.tokopedia.search.databinding.SearchResultProductSmallGridCuratedInspirationCardLayoutBinding
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationCardOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import com.tokopedia.utils.view.binding.viewBinding

class SmallGridInspirationCardViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
) : AbstractViewHolder<InspirationCardDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_small_grid_inspiration_card_layout
    }

    private var inspirationCardBinding: SearchResultProductInspirationCardLayoutBinding? by viewBinding()
    private var inspirationCardCuratedBinding: SearchResultProductSmallGridCuratedInspirationCardLayoutBinding? by viewBinding()

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
            inspirationCardBinding?.inspirationCard?.visibility = View.GONE
            inspirationCardCuratedBinding?.inspirationCardCurated?.visibility = View.VISIBLE
        } else {
            inspirationCardBinding?.inspirationCard?.visibility = View.VISIBLE
            inspirationCardCuratedBinding?.inspirationCardCurated?.visibility = View.GONE
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
            inspirationCardCuratedBinding?.inspirationCardCuratedBackground?.let {
                it.setBackgroundResource(R.drawable.search_background_layer_small_grid_curated_cards)
                it.visibility = View.VISIBLE
            }
        }
        else inspirationCardCuratedBinding?.inspirationCardCuratedBackground?.visibility = View.GONE
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
            it.shouldShowWithAction(element.title.isNotEmpty()) {
                it.text = element.title
            }
        }
    }

    private fun bindContent(element: InspirationCardDataView) {
        val spacingItemDecoration = ChipSpacingItemDecoration(
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        )

        inspirationCardBinding?.recyclerViewInspirationCardOptionList?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.optionData)
            if (!element.isRelated())
                it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun createAdapter(
            inspirationCarouselProductListData: List<InspirationCardOptionDataView>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val inspirationCardOptionAdapter = InspirationCardOptionAdapter(inspirationCardListener)
        inspirationCardOptionAdapter.setItemList(inspirationCarouselProductListData)

        return inspirationCardOptionAdapter
    }

}