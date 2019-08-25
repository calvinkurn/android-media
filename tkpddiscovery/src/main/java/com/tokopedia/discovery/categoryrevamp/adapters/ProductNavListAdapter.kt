package com.tokopedia.discovery.categoryrevamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.BaseProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactory


class ProductNavListAdapter(val productTypeFactory: ProductTypeFactory,
                            val visitables: ArrayList<Visitable<ProductTypeFactory>>,
                            onItemChangeView: OnItemChangeView) : BaseCategoryAdapter(onItemChangeView) {

    private var loadingMoreModel: LoadingMoreModel = LoadingMoreModel()

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun getTypeFactory(): BaseProductTypeFactory {
        return productTypeFactory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        val viewHolder = productTypeFactory.createViewHolder(view, viewType)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(productTypeFactory)
    }

    fun addLoading() {
        val loadingModelPosition = this.visitables.size
        this.visitables.add(loadingMoreModel as Visitable<ProductTypeFactory>)
        notifyItemInserted(loadingModelPosition)
    }

    fun removeLoading() {
        val loadingModelPosition = this.visitables.indexOf(loadingMoreModel as Visitable<ProductTypeFactory>)

        if (loadingModelPosition != -1) {
            this.visitables.remove(loadingMoreModel as Visitable<ProductTypeFactory>)
            notifyItemRemoved(loadingModelPosition)
            notifyItemRangeChanged(loadingModelPosition, 1)
        }
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        visitables.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }
}