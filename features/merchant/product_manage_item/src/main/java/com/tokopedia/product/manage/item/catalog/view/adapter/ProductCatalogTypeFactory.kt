package com.tokopedia.product.manage.item.catalog.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import kotlinx.android.synthetic.main.item_product_edit_catalog.view.*

class ProductCatalogTypeFactory: BaseAdapterTypeFactory(){
    private var listener: OnCatalogClickedListener? = null

    fun setCatalogClickedListener(listener: OnCatalogClickedListener){
        this.listener = listener
    }

    fun type(productCatalog: ProductCatalog): Int = ProductCatalogViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            ProductCatalogViewHolder.LAYOUT -> ProductCatalogViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    class ProductCatalogViewHolder(val view: View?, private val listener: OnCatalogClickedListener?): AbstractViewHolder<ProductCatalog>(view) {
        override fun bind(element: ProductCatalog) {
            itemView.textViewName.text = element.catalogName
            itemView.imageViewCheck.visibility = if (listener?.isItemSelected(adapterPosition) == true) View.VISIBLE else View.GONE
            ImageHandler.LoadImage(itemView.imageViewCatalog, element.catalogImage)
            itemView.setOnClickListener { listener?.onSelected(element, adapterPosition) }
        }

        companion object {
            val LAYOUT = R.layout.item_product_edit_catalog
        }
    }

    interface OnCatalogClickedListener {
        fun isItemSelected(position: Int): Boolean
        fun onSelected(catalog: ProductCatalog, position: Int)
    }
}