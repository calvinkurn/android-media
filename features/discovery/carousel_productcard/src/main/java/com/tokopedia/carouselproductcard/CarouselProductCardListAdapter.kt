package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

internal class CarouselProductCardListAdapter :
        ListAdapter<BaseCarouselCardModel, BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
        CarouselProductCardAdapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(viewType, viewGroup, false)
        return when(viewType){
            CarouselProductCardListViewHolder.LAYOUT -> CarouselProductCardListViewHolder(view)
            else -> CarouselSeeMoreCardGridViewHolder(view)
        } as BaseProductCardViewHolder<BaseCarouselCardModel>
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position) is CarouselProductCardModel) CarouselProductCardListViewHolder.LAYOUT else  CarouselSeeMoreCardGridViewHolder.LAYOUT
    }

    override fun onBindViewHolder(carouselProductCardListViewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>, position: Int) {
        carouselProductCardListViewHolder.bind(getItem(position))
    }

    override fun onViewRecycled(carouselProductCardListViewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>) {
        carouselProductCardListViewHolder.recycle()
        super.onViewRecycled(carouselProductCardListViewHolder)
    }

    override fun asRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun submitCarouselProductCardModelList(list: List<BaseCarouselCardModel>?) {
        submitList(list)
    }
}