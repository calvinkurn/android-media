package com.tokopedia.carouselproductcard

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardListTypeFactoryImpl

internal class CarouselProductCardListAdapter(
    internalListener: CarouselProductCardInternalListener
): ListAdapter<BaseCarouselCardModel,
    BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
    CarouselProductCardAdapter {

    private val adapterTypeFactory = CarouselProductCardListTypeFactoryImpl(internalListener)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        return adapterTypeFactory.onCreateViewHolder(viewGroup, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
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