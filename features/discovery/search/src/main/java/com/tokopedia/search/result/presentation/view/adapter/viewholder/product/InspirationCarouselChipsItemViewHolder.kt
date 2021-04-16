package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView.Option
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.unifycomponents.ChipsUnify

class InspirationCarouselChipsItemViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_carousel_chips_item
    }

    private var chipsUnify: ChipsUnify? = null

    init {
        chipsUnify = itemView.findViewById(R.id.inspirationCarouselChipsUnify)
    }

    fun bind(
            inspirationCarouselAdapterPosition: Int,
            inspirationCarouselViewModel: InspirationCarouselDataView,
            inspirationCarouselOption: Option,
    ) {
        chipsUnify?.chipType = getChipType(inspirationCarouselOption)
        chipsUnify?.chipSize = ChipsUnify.SIZE_SMALL
        chipsUnify?.chipText = inspirationCarouselOption.title
        chipsUnify?.setOnClickListener {
            onChipsClicked(
                    inspirationCarouselAdapterPosition,
                    inspirationCarouselViewModel,
                    inspirationCarouselOption
            )
        }
    }

    private fun getChipType(inspirationCarouselOption: Option) =
            if (inspirationCarouselOption.isChipsActive) ChipsUnify.TYPE_SELECTED
            else ChipsUnify.TYPE_NORMAL

    private fun onChipsClicked(
            inspirationCarouselAdapterPosition: Int,
            inspirationCarouselViewModel: InspirationCarouselDataView,
            inspirationCarouselOption: Option,
    ) {
        if (inspirationCarouselOption.isChipsActive) return

        inspirationCarouselListener.onInspirationCarouselChipsClicked(
                inspirationCarouselAdapterPosition,
                inspirationCarouselViewModel,
                inspirationCarouselOption,
        )
    }
}