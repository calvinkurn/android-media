package com.tokopedia.carouselproductcard

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardGridTypeFactoryImpl

internal class CarouselProductCardGridAdapter(
    internalListener: CarouselProductCardInternalListener,
    isReimagine: Boolean,
): ListAdapter<BaseCarouselCardModel,
    BaseProductCardViewHolder<BaseCarouselCardModel>>(ProductCardModelDiffUtil()),
    CarouselProductCardAdapter {

    private val adapterTypeFactory = CarouselProductCardGridTypeFactoryImpl(internalListener, isReimagine)
    private val scrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(scrollListener)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        return adapterTypeFactory.onCreateViewHolder(viewGroup, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }

    override fun onBindViewHolder(baseProductCardViewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>, position: Int) {
        setOnAttachStateChangeListener(baseProductCardViewHolder)
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

    private fun setOnAttachStateChangeListener(viewHolder: BaseProductCardViewHolder<BaseCarouselCardModel>) {
        val onAttachStateChangeListener: View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                if (viewHolder.bindingAdapterPosition > RecyclerView.NO_POSITION) {
                    viewHolder.onViewAttachedToWindow()
                }
            }

            override fun onViewDetachedFromWindow(view: View) {
                if (viewHolder.bindingAdapterPosition > RecyclerView.NO_POSITION) {
                    viewHolder.onViewDetachedFromWindow(viewHolder.visiblePercentage)
                }
            }
        }
        viewHolder.itemView.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }
}
