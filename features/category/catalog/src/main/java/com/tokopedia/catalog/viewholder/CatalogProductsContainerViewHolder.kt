package com.tokopedia.catalog.viewholder

import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogPagerAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel

class CatalogProductsContainerViewHolder(private val view: View,
                                         private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    private val viewPager: ViewPager = itemView.findViewById(R.id.view_pager)
    private val container: View = itemView.findViewById(R.id.root_container)
    private val context: Context = itemView.context
    private var catalogPageAdapter : CatalogPagerAdapter? = null
    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {
        val layoutParams = container.layoutParams
        layoutParams.height = catalogDetailListener.windowHeight
        container.layoutParams = layoutParams

        initViewPager(element)
    }

    private fun initViewPager(element: CatalogProductsContainerDataModel){
        catalogPageAdapter = CatalogPagerAdapter(catalogDetailListener,
                element,catalogDetailListener.childsFragmentManager!!)

        viewPager.adapter = catalogPageAdapter
    }
}