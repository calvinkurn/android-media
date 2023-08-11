package com.tokopedia.oldcatalog.viewholder.containers

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.adapter.CatalogPagerAdapter
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogProductsContainerDataModel

class CatalogProductsContainerViewHolder(view: View,
                                         private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    private val viewPager: ViewPager? = itemView.findViewById(R.id.view_pager)
    private var catalogPageAdapter : CatalogPagerAdapter? = null
    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {
        if(catalogDetailListener.getChildsFragmentManager() != null){
            catalogPageAdapter = CatalogPagerAdapter(
                element,catalogDetailListener.getChildsFragmentManager()!!)
            viewPager?.adapter = catalogPageAdapter
        }
    }
}
