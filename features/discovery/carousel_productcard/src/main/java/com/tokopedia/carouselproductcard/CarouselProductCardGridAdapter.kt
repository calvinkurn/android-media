package com.tokopedia.carouselproductcard

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardGridTypeFactoryImpl

internal class CarouselProductCardGridAdapter(
    internalListener: CarouselProductCardInternalListener
): ListAdapter<BaseCarouselCardModel,
    BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
    CarouselProductCardAdapter {

    private val adapterTypeFactory = CarouselProductCardGridTypeFactoryImpl(internalListener)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        return adapterTypeFactory.onCreateViewHolder(viewGroup, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
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