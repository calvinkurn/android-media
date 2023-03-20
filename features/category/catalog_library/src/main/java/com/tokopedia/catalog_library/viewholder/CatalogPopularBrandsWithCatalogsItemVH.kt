package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsListDM
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class CatalogPopularBrandsWithCatalogsItemVH(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogPopularBrandsListDM>(view) {

    private var dataModel: CatalogPopularBrandsListDM? = null

    private val brandIcon: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.img_brand_icon)
    }

    private val catalogImage1: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.img_catalog_1)
    }

    private val catalogImage2: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.img_catalog_2)
    }

    private val catalogImage3: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.img_catalog_3)
    }

    private val brandName: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.tv_brand_name)
    }

    private val lihatButton: UnifyButton by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.tv_lihat_brand)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_popular_brands_with_catalogs
    }

    override fun bind(element: CatalogPopularBrandsListDM?) {
        dataModel = element
        brandIcon.loadImage(dataModel?.brandsList?.imageUrl)
        brandName.text = dataModel?.brandsList?.name
        dataModel?.brandsList?.catalogs?.forEachIndexed { index, catalog ->
            when(index){
                0 -> catalogImage1.loadImage(catalog.imageUrl)
                1 -> catalogImage2.loadImage(catalog.imageUrl)
                2 -> catalogImage3.loadImage(catalog.imageUrl)
            }
        }

        brandIcon.setOnClickListener{ onClickPopularBrand() }
        brandName.setOnClickListener{ onClickPopularBrand() }
        lihatButton.setOnClickListener{ onClickPopularBrand() }
    }

    private fun onClickPopularBrand() {
        catalogLibraryListener.onPopularBrandsClick("tokopedia://catalog-library/brand/${dataModel?.brandsList?.id}")
    }

    override fun onViewAttachedToWindow() {
        dataModel?.brandsList?.let {

        }
    }
}
