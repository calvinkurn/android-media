package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

internal class CarouselProductCardListAdapter :
        ListAdapter<CarouselProductCardModel, CarouselProductCardListViewHolder>(ProductCardModelDiffUtil()),
        CarouselProductCardAdapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CarouselProductCardListViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(CarouselProductCardListViewHolder.LAYOUT, viewGroup, false)
        return CarouselProductCardListViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return CarouselProductCardListViewHolder.LAYOUT
    }

    override fun onBindViewHolder(carouselProductCardListViewHolder: CarouselProductCardListViewHolder, position: Int) {
        carouselProductCardListViewHolder.bind(getItem(position))
    }

    override fun onViewRecycled(carouselProductCardListViewHolder: CarouselProductCardListViewHolder) {
        carouselProductCardListViewHolder.recycle()
        super.onViewRecycled(carouselProductCardListViewHolder)
    }

    override fun asRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun submitCarouselProductCardModelList(list: List<CarouselProductCardModel>?) {
        submitList(list)
    }
}