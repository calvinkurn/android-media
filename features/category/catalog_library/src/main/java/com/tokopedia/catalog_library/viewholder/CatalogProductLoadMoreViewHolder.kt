package com.tokopedia.catalog_library.viewholder

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_LIHAT_SEMUA
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_VIRAL
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class CatalogProductLoadMoreViewHolder(val view: View) :
    AbstractViewHolder<CatalogShimmerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_product_load_more
    }

    override fun bind(element: CatalogShimmerDataModel?) {

    }
}
