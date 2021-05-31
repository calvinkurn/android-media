package com.tokopedia.product.detail.view.adapter.dynamicadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductMediaViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder

/**
 * Created by Yehezkiel on 04/01/21
 */
class ProductDetailAdapter(asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel>,
                           private val listener: DynamicProductDetailListener?,
                           private val adapterTypeFactory: DynamicProductDetailAdapterFactory) :
        ListAdapter<DynamicPdpDataModel, AbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<DynamicPdpDataModel>, getItem(position))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bind(holder as AbstractViewHolder<DynamicPdpDataModel>, getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(adapterTypeFactory) ?: HideViewHolder.LAYOUT
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ProductRecommendationViewHolder &&
                holder.adapterPosition < currentList.size &&
                (currentList[holder.adapterPosition] as? ProductRecommendationDataModel)?.recomWidgetData == null) {
            listener?.loadTopads((currentList[holder.adapterPosition] as ProductRecommendationDataModel).name)
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ProductMediaViewHolder) {
            holder.detachView()
        }
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel) {
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel, payloads: MutableList<Any>) {
        val payloadInt = (payloads.firstOrNull() as? Bundle)?.getInt(ProductDetailConstant.DIFFUTIL_PAYLOAD)
        if (payloads.isNotEmpty() && payloads.firstOrNull() != null && payloadInt != null) {
            holder.bind(item, listOf(payloadInt))
        } else {
            holder.bind(item)
        }
    }

    fun showLoading() {
        if (!isLoading()) {
            submitList(listOf(ProductLoadingDataModel()))
        }
    }

    fun showError(data: PageErrorDataModel) {
        submitList(listOf(data))
    }

    private fun isLoading(): Boolean {
        val lastIndex = if (currentList.size == 0) -1 else currentList.size -1
        return if (lastIndex > -1) {
            currentList[lastIndex] is LoadingModel ||
                    currentList[lastIndex] is LoadingMoreModel ||
                    currentList[lastIndex] is ProductLoadingDataModel
        } else {
            false
        }
    }
}