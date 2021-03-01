package com.tokopedia.catalog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.catalog.viewholder.CatalogInfoViewHolder
import com.tokopedia.catalog.viewholder.CatalogProductsContainerViewHolder
import kotlinx.android.synthetic.main.item_catalog_products_container.view.*

class CatalogDetailAdapter (val context : FragmentActivity, val catalogDetailListener: CatalogDetailListener ,asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel>,
                            private val catalogAdapterTypeFactory: CatalogDetailAdapterFactory)
    :ListAdapter<BaseCatalogDataModel, AbstractViewHolder<*>>(asyncDifferConfig){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return catalogAdapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseCatalogDataModel>, getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(holder: AbstractViewHolder<BaseCatalogDataModel>, item: BaseCatalogDataModel) {
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(catalogAdapterTypeFactory) ?: HideViewHolder.LAYOUT
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        if(holder is CatalogInfoViewHolder){
            catalogDetailListener.showFloatingLayout()
            //attachFragmentToContainer(R.id.products_container_frame)
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<*>) {
        if(holder is CatalogInfoViewHolder){
            catalogDetailListener.hideFloatingLayout()
        }
        super.onViewDetachedFromWindow(holder)

    }

    private fun attachFragmentToContainer(containerId: Int) {
        val fragment = if (context.supportFragmentManager.fragments.firstOrNull { it is CatalogDetailProductListingFragment } == null)
            CatalogDetailProductListingFragment.newInstance("65051","","","")
        else
            null

        if (fragment != null) {
            context.supportFragmentManager.beginTransaction()
                    .add(containerId, fragment)
                    .commitNowAllowingStateLoss()
        }
    }
}