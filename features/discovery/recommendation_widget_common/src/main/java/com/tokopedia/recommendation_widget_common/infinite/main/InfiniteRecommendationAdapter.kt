package com.tokopedia.recommendation_widget_common.infinite.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.AdapterDelegatesManager
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingDelegate
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingUiModel
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingViewHolder
import com.tokopedia.recommendation_widget_common.infinite.component.product.InfiniteProductDelegate
import com.tokopedia.recommendation_widget_common.infinite.component.product.InfiniteProductViewHolder
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.utils.InfiniteDiffItemCallback

class InfiniteRecommendationAdapter(
    private val callback: InfiniteRecommendationCallback
) : ListAdapter<InfiniteRecommendationUiModel, InfiniteRecommendationViewHolder<*>>(
    InfiniteDiffItemCallback
) {

    private val delegatesManager = AdapterDelegatesManager<InfiniteRecommendationUiModel>()
    private val singleLoadingItem = listOf(InfiniteLoadingUiModel)

    init {
        delegatesManager
            .addDelegate(InfiniteLoadingDelegate())
            .addDelegate(InfiniteProductDelegate())

        submitList(singleLoadingItem)
    }

    fun init() {
        submitList(singleLoadingItem)
    }

    private fun determineFullSpan(holder: InfiniteRecommendationViewHolder<*>) {
        val isFullSpan = holder !is InfiniteProductViewHolder
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)
            ?.isFullSpan = isFullSpan
    }

    override fun onViewAttachedToWindow(holder: InfiniteRecommendationViewHolder<*>) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is InfiniteLoadingViewHolder -> callback.fetchRecommendation()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfiniteRecommendationViewHolder<*> {
        return delegatesManager.onCreateViewHolder(
            parent,
            viewType
        ) as InfiniteRecommendationViewHolder<*>
    }

    override fun onBindViewHolder(holder: InfiniteRecommendationViewHolder<*>, position: Int) {
        determineFullSpan(holder)
        delegatesManager.onBindViewHolder(currentList, position, holder)
    }

    override fun onBindViewHolder(
        holder: InfiniteRecommendationViewHolder<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            delegatesManager.onBindViewHolder(
                currentList,
                position,
                holder,
                payloads = payloads.filterIsInstance<Bundle>().reduce { acc, bundle ->
                    acc.apply { putAll(bundle) }
                }
            )
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(currentList, position)
    }

}
