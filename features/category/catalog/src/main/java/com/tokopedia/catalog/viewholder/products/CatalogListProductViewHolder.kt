package com.tokopedia.catalog.viewholder.products

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_catalog_preffered_product.view.*


open class CatalogListProductViewHolder(itemView: View, private val catalogProductCardListener: CatalogProductCardListener) : AbstractViewHolder<CatalogProductItem>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_catalog_preffered_product
    }

    override fun bind(catalogProductItem: CatalogProductItem?) {
        if (catalogProductItem == null) return

        itemView.setOnClickListener {
            catalogProductCardListener.onItemClicked(catalogProductItem, adapterPosition)
        }
        itemView.product_image.loadImageRounded(catalogProductItem.imageUrl)
        itemView.product_name.displayTextOrHide(catalogProductItem.name)
        itemView.product_price.displayTextOrHide(catalogProductItem.priceString)
        itemView.shop_name.displayTextOrHide(catalogProductItem.shop.name)
        itemView.imageThreeDots.setOnClickListener {
            catalogProductCardListener.onThreeDotsClicked(catalogProductItem, adapterPosition)
        }

        if(catalogProductItem.freeOngkir.isActive){
            itemView.image_ongir.show()
            itemView.image_ongir.loadImageWithoutPlaceholder(catalogProductItem.freeOngkir.imageUrl)
        }else {
            itemView.image_ongir.hide()
        }

        if(catalogProductItem.ratingAverage.isNotBlank()){
            itemView.rating_float.displayTextOrHide(catalogProductItem.ratingAverage)
            itemView.icon_rating.show()
            itemView.rating_line.show()
        }else {
            itemView.rating_float.hide()
            itemView.icon_rating.hide()
            itemView.rating_line.hide()
        }

        showBadge(catalogProductItem)
        showTerjual(catalogProductItem)
    }

    private fun showTerjual(catalogProductItem: CatalogProductItem?){
        val integrity = catalogProductItem?.labelGroupList?.find { it.position == CatalogConstant.INTEGRITY_GROUP }
        integrity?.let {
            if (it.title.isNotBlank()){
                itemView.sales_tv.show()
                itemView.sales_tv.displayTextOrHide(it.title)
            }else {
                itemView.sales_tv.hide()
            }
        }
    }

    private fun showBadge(catalogProductItem: CatalogProductItem?){
        val badge = catalogProductItem?.badgeList?.find { it.show }
        badge?.let {
            if(!it.imageURL.isNullOrBlank()){
                itemView.icon_shop_name.show()
                itemView.icon_shop_name.loadImage(it.imageURL!!)
            }else {
                itemView.icon_shop_name.hide()
            }
        }
    }
}