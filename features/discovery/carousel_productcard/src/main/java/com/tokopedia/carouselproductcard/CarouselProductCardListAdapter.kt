package com.tokopedia.carouselproductcard

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.PercentageScrollListener
import com.tokopedia.abstraction.base.view.recyclerview.listener.IAdsViewHolderTrackListener
import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardListTypeFactoryImpl

internal class CarouselProductCardListAdapter(
    internalListener: CarouselProductCardInternalListener
): ListAdapter<BaseCarouselCardModel,
    BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
    CarouselProductCardAdapter {

    private val adapterTypeFactory = CarouselProductCardListTypeFactoryImpl(internalListener)
    private val scrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.recyclerView?.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView?.removeOnScrollListener(scrollListener)
        this.recyclerView = null
    }

    override fun onViewAttachedToWindow(holder: BaseProductCardViewHolder<BaseCarouselCardModel>) {
        super.onViewAttachedToWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            holder.onViewAttachedToWindow()
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseProductCardViewHolder<BaseCarouselCardModel>) {
        super.onViewDetachedFromWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            holder.onViewDetachedFromWindow(holder.visiblePercentage)
        }
    }

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
