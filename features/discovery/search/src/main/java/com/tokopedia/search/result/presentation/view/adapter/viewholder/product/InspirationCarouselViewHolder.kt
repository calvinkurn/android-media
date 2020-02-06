package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselProductAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel.view.*

class InspirationCarouselViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel>(itemView) {

    override fun bind(element: InspirationCarouselViewModel) {
        setBackgroundRandom()
        itemView.inspirationCarousel?.inspirationCarouselTitle?.text = element.title
        itemView.inspirationCarousel?.inspirationCarouselOptionList?.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        itemView.inspirationCarousel?.inspirationCarouselOptionList?.adapter =
                createAdapter(element.options, inspirationCarouselListener)
    }

    private fun setBackgroundRandom() {
        val backgroundIndex = intArrayOf(0, 1, 2, 3).indices.shuffled().first()
        itemView.inspirationCarousel?.setContainerColor(backgroundIndex)
    }

    private fun createAdapter(
            inspirationCarouselProductList: List<InspirationCarouselViewModel.Option>,
            inspirationCarouselListener: InspirationCarouselListener
    ): RecyclerView.Adapter<*> {
        val inspirationCarouselProductAdapter = InspirationCarouselProductAdapter(inspirationCarouselListener)
        inspirationCarouselProductAdapter.setItemList(inspirationCarouselProductList)

        return inspirationCarouselProductAdapter
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel
    }
}