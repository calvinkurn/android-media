package com.tokopedia.catalog.viewholder

import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogPagerAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment

class CatalogProductsContainerViewHolder(view: View,
                                         private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    private val container: View = itemView.findViewById(R.id.root_container)
    private val context: Context = itemView.context
    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {
        val layoutParams = container.layoutParams
        layoutParams.height = catalogDetailListener.windowHeight - context.resources.getDimensionPixelSize(R.dimen.dp_8)
        container.layoutParams = layoutParams

        CatalogDetailProductListingFragment.newInstance(element.catalogId).apply{
            catalogDetailListener.childsFragmentManager?.beginTransaction()?.replace(R.id.products_container_frame,
                    this)?.commit()
        }
    }
}