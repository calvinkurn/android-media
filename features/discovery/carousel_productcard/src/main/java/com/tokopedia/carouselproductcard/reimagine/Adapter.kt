package com.tokopedia.carouselproductcard.reimagine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.carouselproductcard.BaseCarouselCardModel
import com.tokopedia.carouselproductcard.BaseProductCardViewHolder

private typealias CarouselProductCardAdapter =
    ListAdapter<Visitable<CarouselProductCardTypeFactory>, AbstractViewHolder<*>>

internal class Adapter(
    private val typeFactory: CarouselProductCardTypeFactory,
    private val onCurrentListChanged: () -> Unit,
): CarouselProductCardAdapter(DiffUtilItemCallback(typeFactory)) {

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return typeFactory.onCreateViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        setOnAttachStateChangeListener(holder)
        @Suppress("UNCHECKED_CAST")
        try { (holder as AbstractViewHolder<Visitable<*>>).bind(getItem(position)) }
        catch(_: Throwable) { }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type(typeFactory)

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        holder.onViewRecycled()
    }

    fun getItemAt(position: Int): Visitable<CarouselProductCardTypeFactory>? {
        if (position !in currentList.indices) return null
        return getItem(position)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Visitable<CarouselProductCardTypeFactory>>,
        currentList: MutableList<Visitable<CarouselProductCardTypeFactory>>
    ) {
        super.onCurrentListChanged(previousList, currentList)

        onCurrentListChanged()
    }

    private fun setOnAttachStateChangeListener(viewHolder: AbstractViewHolder<*>) {
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
        (viewHolder as? AbstractViewHolder<Visitable<*>>)?.itemView?.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }

}
