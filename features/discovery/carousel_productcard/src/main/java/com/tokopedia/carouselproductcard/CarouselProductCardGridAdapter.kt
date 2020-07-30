package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

internal class CarouselProductCardGridAdapter :
        ListAdapter<CarouselProductCardModel, CarouselProductCardGridViewHolder>(ProductCardModelDiffUtil()),
        CarouselProductCardAdapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CarouselProductCardGridViewHolder {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(CarouselProductCardGridViewHolder.LAYOUT, viewGroup, false)
        return CarouselProductCardGridViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return CarouselProductCardGridViewHolder.LAYOUT
    }

    override fun onBindViewHolder(carouselProductCardGridViewHolder: CarouselProductCardGridViewHolder, position: Int) {
        carouselProductCardGridViewHolder.bind(getItem(position))
    }

    override fun onViewRecycled(carouselProductCardGridViewHolder: CarouselProductCardGridViewHolder) {
        carouselProductCardGridViewHolder.recycle()
        super.onViewRecycled(carouselProductCardGridViewHolder)
    }

    override fun asRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun submitCarouselProductCardModelList(list: List<CarouselProductCardModel>?) {
        submitList(list)
    }
}