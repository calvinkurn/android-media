package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailCatalogTitleBinding
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.share.ekstensions.layoutInflater

class ProductDetailInfoCatalogViewHolder(
    view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoCatalogDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_catalog_title
    }

    private val binding = BsItemProductDetailCatalogTitleBinding.bind(view)

    override fun bind(element: ProductDetailInfoCatalogDataModel) = with(binding) {
        txtTitle.text = element.title

        renderList(element)
    }

    private fun renderList(element: ProductDetailInfoCatalogDataModel) = with(binding) {
        containerCatalogList.showIfWithBlock(element.items.isNotEmpty()) {
            val inflater = context.layoutInflater
            removeAllViews()

            element.items.forEach { catalog ->
                val view = ItemInfoProductDetailBinding.inflate(inflater, binding.root, false)
                view.infoDetailTitle.text = catalog.key
                view.infoDetailValue.text = catalog.value
                view.infoDetailValue.maxLines = Int.MAX_VALUE
                addView(view.root)
            }
        }
    }

    private fun onImpressView() {
        listener.onImpressCatalog()
    }
}