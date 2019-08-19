package com.tokopedia.discovery.categoryrevamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.BaseProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactory


class ProductNavListAdapter(val productTypeFactory: ProductTypeFactory,
                            val visitables: ArrayList<Visitable<ProductTypeFactory>>,
                            onItemChangeView: OnItemChangeView) : BaseCategoryAdapter(onItemChangeView) {


    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    fun appendList(visitables: ArrayList<Visitable<ProductTypeFactory>>) {
        this.visitables.addAll(visitables)
    }


    override fun getTypeFactory(): BaseProductTypeFactory {
        return productTypeFactory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        val viewHolder = productTypeFactory.createViewHolder(view, viewType)
        //setTopAdsSwitcherForTopAdsViewHolder(viewHolder)p1
        return viewHolder
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(productTypeFactory)
    }
}