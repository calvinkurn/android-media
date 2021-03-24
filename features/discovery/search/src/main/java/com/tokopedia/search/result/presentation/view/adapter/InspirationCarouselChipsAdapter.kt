package com.tokopedia.search.result.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselChipsItemViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener

class InspirationCarouselChipsAdapter(
        private val inspirationCarouselAdapterPosition: Int,
        private val inspirationCarouselViewModel: InspirationCarouselDataView,
        private val inspirationCarouselListener: InspirationCarouselListener,
): RecyclerView.Adapter<InspirationCarouselChipsItemViewHolder>() {

    private val optionList = inspirationCarouselViewModel.options

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationCarouselChipsItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(InspirationCarouselChipsItemViewHolder.LAYOUT, parent, false)

        return InspirationCarouselChipsItemViewHolder(view, inspirationCarouselListener)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: InspirationCarouselChipsItemViewHolder, position: Int) {
        holder.bind(inspirationCarouselAdapterPosition, inspirationCarouselViewModel, optionList[position])
    }
}