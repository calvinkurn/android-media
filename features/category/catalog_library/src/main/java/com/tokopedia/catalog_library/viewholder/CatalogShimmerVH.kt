package com.tokopedia.catalog_library.viewholder

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDM
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_LIHAT_SEMUA
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_POPULAR_BRAND
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_VIRAL
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class CatalogShimmerVH(val view: View) :
    CatalogLibraryAbstractViewHolder<CatalogShimmerDM>(view) {
    companion object {
        val LAYOUT = R.layout.item_catalog_library_shimmer_product
    }

    override fun bind(element: CatalogShimmerDM?) {
        when (element?.shimmerType) {
            CATALOG_SHIMMER_TOP_FIVE -> {
                view.findViewById<LinearLayout>(R.id.top_five_shimmer_ll).show()
                view.findViewById<LinearLayout>(R.id.viral_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.products_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.grid_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.popular_brand_ll).hide()
            }
            CATALOG_SHIMMER_VIRAL -> {
                view.findViewById<LinearLayout>(R.id.top_five_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.viral_shimmer_ll).show()
                view.findViewById<LinearLayout>(R.id.products_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.grid_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.popular_brand_ll).hide()
            }
            CATALOG_SHIMMER_PRODUCTS -> {
                view.findViewById<LinearLayout>(R.id.top_five_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.viral_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.products_shimmer_ll).show()
                view.findViewById<LinearLayout>(R.id.grid_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.popular_brand_ll).hide()
            }
            CATALOG_SHIMMER_LIHAT_SEMUA -> {
                view.findViewById<LinearLayout>(R.id.top_five_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.viral_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.products_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.grid_shimmer_ll).show()
                view.findViewById<LinearLayout>(R.id.popular_brand_ll).hide()
            }
            CATALOG_SHIMMER_POPULAR_BRAND -> {
                view.findViewById<LinearLayout>(R.id.top_five_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.viral_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.products_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.grid_shimmer_ll).hide()
                view.findViewById<LinearLayout>(R.id.popular_brand_ll).show()
            }
        }
    }
}
