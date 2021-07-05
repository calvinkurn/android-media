package com.tokopedia.product.info.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable

/**
 * Created by Yehezkiel on 12/10/20
 */
open class BsProductDetailInfoBaseAdapter(asyncDifferConfig: AsyncDifferConfig<ProductDetailInfoVisitable>, private val adapterTypeFactory: ProductDetailInfoAdapterFactory) :
        ListAdapter<ProductDetailInfoVisitable, AbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<ProductDetailInfoVisitable>, getItem(position))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bind(holder as AbstractViewHolder<ProductDetailInfoVisitable>, getItem(position), payloads)
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

    fun bind(holder: AbstractViewHolder<ProductDetailInfoVisitable>, item: ProductDetailInfoVisitable) {
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<ProductDetailInfoVisitable>, item: ProductDetailInfoVisitable, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(item, payloads)
        }
    }
}