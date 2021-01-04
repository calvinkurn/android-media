package com.tokopedia.product.detail.view.adapter.dynamicadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLoadingDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 04/01/21
 */
class ProductDetailAdapter(asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel>, private val adapterTypeFactory: DynamicProductDetailAdapterFactory) :
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
        } else currentList[position].type(adapterTypeFactory)
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel) {
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(item, payloads)
        }
    }

    fun showLoading() {
        submitList(listOf(ProductLoadingDataModel()))
    }

    fun showError(data: PageErrorDataModel) {
        submitList(listOf(data))
    }
}