package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsListDM
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.APP_LINK_BRANDS
import com.tokopedia.media.loader.loadImage
import com.tokopedia.track.builder.Tracker
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

    private val catalogText1: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.tv_catalog_1_name)
    }

    private val catalogText2: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.tv_catalog_2_name)
    }

    private val catalogText3: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.tv_catalog_3_name)
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
                0 -> {
                    catalogText1.text = catalog.name
                    catalogImage1.loadImage(catalog.imageUrl)
                    catalogText1.setOnClickListener { onClickPopularBrand(dataModel?.brandsList?.name,
                        dataModel?.brandsList?.id,
                        (bindingAdapterPosition + 1).toString(),
                        catalog.name,
                        catalog.id, catalog.applink
                        )
                    }
                    catalogImage1.setOnClickListener {
                        onClickPopularBrand(dataModel?.brandsList?.name,
                            dataModel?.brandsList?.id,
                            (bindingAdapterPosition + 1).toString(),
                            catalog.name,
                            catalog.id,catalog.applink
                        )
                    }
                }
                1 -> {
                    catalogText2.text = catalog.name
                    catalogImage2.loadImage(catalog.imageUrl)
                    catalogText2.setOnClickListener {
                        onClickPopularBrand(dataModel?.brandsList?.name,
                            dataModel?.brandsList?.id,
                            (bindingAdapterPosition + 1).toString(),
                            catalog.name,
                            catalog.id,catalog.applink
                        )
                    }
                    catalogImage2.setOnClickListener {
                        onClickPopularBrand(dataModel?.brandsList?.name,
                            dataModel?.brandsList?.id,
                            (bindingAdapterPosition + 1).toString(),
                            catalog.name,
                            catalog.id,catalog.applink
                        )
                    }
                }
                2 -> {
                    catalogText3.text = catalog.name
                    catalogImage3.loadImage(catalog.imageUrl)
                    catalogText3.setOnClickListener {
                        onClickPopularBrand(dataModel?.brandsList?.name,
                            dataModel?.brandsList?.id,
                            (bindingAdapterPosition + 1).toString(),
                            catalog.name,
                            catalog.id,catalog.applink
                        )
                    }
                    catalogImage3.setOnClickListener {onClickPopularBrand(dataModel?.brandsList?.name,
                        dataModel?.brandsList?.id,
                        (bindingAdapterPosition + 1).toString(),
                        catalog.name,
                        catalog.id,catalog.applink
                    ) }
                }
            }
        }

        brandIcon.setOnClickListener{ onClickLihatSemua(dataModel?.brandsList?.name,dataModel?.brandsList?.id,
            (bindingAdapterPosition + 1).toString(),
            "click on brand logo","37370") }
        brandName.setOnClickListener{  onClickLihatSemua(dataModel?.brandsList?.name,dataModel?.brandsList?.id,
            (bindingAdapterPosition + 1).toString(),
            "click on brand name","37370") }
        lihatButton.setOnClickListener{  onClickLihatSemua(dataModel?.brandsList?.name,dataModel?.brandsList?.id,
            (bindingAdapterPosition + 1).toString(),
            "click on lihat button","37369") }
    }

    private fun onClickLihatSemua(brandName: String?, brandId: String?, position : String,
                                  eventAction : String, trackerId : String) {
        catalogLibraryListener.onPopularBrandsLihatSemuaClick(brandName ?: "",
            brandId ?: "",position, eventAction, trackerId)
    }

    private fun onClickPopularBrand(brandName: String?, brandId: String?, position : String,
                                    catalogName: String?, catalogId: String?, applink : String?){
        catalogLibraryListener.onPopularBrandsClick(brandName ?: "",brandId?:"",
            position, catalogName?:"", catalogId?:"", applink ?: "")
    }

    override fun onViewAttachedToWindow() {
        dataModel?.brandsList?.let {

        }

    }
}
