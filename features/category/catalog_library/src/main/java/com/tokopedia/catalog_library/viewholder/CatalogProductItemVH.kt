package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogProductDM
import com.tokopedia.catalog_library.util.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogProductItemVH(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : CatalogLibraryAbstractViewHolder<CatalogProductDM>(view) {

    private var dataModel: CatalogProductDM? = null

    private val productImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_image)
    }

    private val productTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_title)
    }

    private val productPrice: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_price)
    }

    private val productLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.product_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_product
    }

    override fun bind(element: CatalogProductDM?) {
        dataModel = element
        renderProductPrice()
        renderProductImage()
        renderProductTitle()
        setUpClick()
    }
    private fun renderProductTitle() {
        productTitle.text = dataModel?.catalogProduct?.name ?: ""
    }

    private fun renderProductPrice() {
        productPrice.text = String.format(
            itemView.context.getString(R.string.product_price),
            dataModel?.catalogProduct?.marketPrice?.minFmt,
            dataModel?.catalogProduct?.marketPrice?.maxFmt
        )
    }

    private fun renderProductImage() {
        dataModel?.catalogProduct?.imageUrl?.let { iconUrl ->
            productImage.loadImage(iconUrl)
        }
    }

    private fun setUpClick() {
        productLayout.setOnClickListener {
            dataModel?.catalogProduct?.let { it1 ->
                when (dataModel?.source) {
                    CatalogLibraryConstant.SOURCE_HOMEPAGE -> {
                        CatalogAnalyticsHomePage.sendClickCatalogOnCatalogListEvent(
                            it1,
                            layoutPosition - 2,
                            UserSession(itemView.context).userId
                        )
                    }
                    CatalogLibraryConstant.SOURCE_CATEGORY_LANDING_PAGE -> {
                        CatalogAnalyticsCategoryLandingPage.sendClickCatalogOnCatalogListEvent(
                            "category: ${dataModel?.categoryName} - ${it1.categoryID} - catalog: ${it1.name} - ${it1.id}",
                            ActionKeys.CLICK_ON_CATALOG_LIST_IN_CATEGORY,
                            CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE,
                            TrackerId.CLICK_ON_CATALOG_LIST_IN_CATEGORY,
                            it1,
                            layoutPosition - 2,
                            UserSession(itemView.context).userId
                        )
                    }
                    CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE -> {
                        CatalogAnalyticsCategoryLandingPage.sendClickCatalogOnCatalogListEvent(
                            "category: ${dataModel?.categoryName} - ${it1.categoryID} - catalog: ${it1.name} - ${it1.id}",
                            ActionKeys.CLICK_ON_CATALOG,
                            CategoryKeys.CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE,
                            TrackerId.CLICK_ON_CATALOG_LIST_IN_BRAND,
                            it1,
                            layoutPosition - 2,
                            UserSession(itemView.context).userId
                        )
                    }
                }
            }
            catalogLibraryListener.onProductCardClicked(dataModel?.catalogProduct?.applink)
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.catalogProduct?.let {
            when (dataModel?.source) {
                CatalogLibraryConstant.SOURCE_HOMEPAGE -> {
                    catalogLibraryListener.catalogProductsHomePageImpression(
                        dataModel?.categoryName ?: "",
                        it,
                        layoutPosition - 2,
                        UserSession(itemView.context).userId
                    )
                }
                CatalogLibraryConstant.SOURCE_CATEGORY_LANDING_PAGE -> {
                    catalogLibraryListener.catalogProductsCategoryLandingImpression(
                        dataModel?.categoryName ?: "",
                        it,
                        layoutPosition - 2,
                        UserSession(itemView.context).userId
                    )
                }
                CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE -> {
                    catalogLibraryListener.catalogProductsCategoryLandingImpression(
                        dataModel?.categoryName ?: "",
                        it,
                        layoutPosition - 2,
                        UserSession(itemView.context).userId
                    )
                }
            }
        }
    }
}
