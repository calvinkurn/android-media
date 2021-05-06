package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogPagerAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel

class CatalogProductsContainerViewHolder(view: View,
                                         private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    private val viewPager: ViewPager = itemView.findViewById(R.id.view_pager)
    private var catalogPageAdapter : CatalogPagerAdapter? = null
    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {
        catalogPageAdapter = CatalogPagerAdapter(
                element,catalogDetailListener.childsFragmentManager!!)
        viewPager.adapter = catalogPageAdapter
    }
}