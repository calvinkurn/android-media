package com.tokopedia.carouselproductcard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

internal class CarouselProductCardGridAdapter :
        ListAdapter<BaseCarouselCardModel, BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
        CarouselProductCardAdapter {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(viewType, viewGroup, false)
        return when(viewType){
            CarouselProductCardGridViewHolder.LAYOUT -> CarouselProductCardGridViewHolder(view)
            else -> CarouselSeeMoreCardGridViewHolder(view)
        } as BaseProductCardViewHolder<BaseCarouselCardModel>
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position) is CarouselProductCardModel) CarouselProductCardGridViewHolder.LAYOUT else  CarouselSeeMoreCardGridViewHolder.LAYOUT
    }

    override fun onBindViewHolder(baseProductCardViewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>, position: Int) {
        baseProductCardViewHolder.bind(getItem(position))
    }

    override fun onViewRecycled(carouselProductCardGridViewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>) {
        carouselProductCardGridViewHolder.recycle()
        super.onViewRecycled(carouselProductCardGridViewHolder)
    }

    override fun asRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun submitCarouselProductCardModelList(list: List<BaseCarouselCardModel>?) {
        submitList(list)
    }
}