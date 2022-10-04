package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.databinding.BsItemProductDetailCatalogTitleBinding
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.info.data.response.ItemCatalog
import com.tokopedia.product.info.view.models.ProductDetailInfoCatalogDataModel
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
                val itemView = createSubView(inflater = inflater, catalog = catalog)
                onImpressView(catalog = catalog)
                addView(itemView)
            }
        }
    }

    private fun createSubView(inflater: LayoutInflater, catalog: ItemCatalog): View {
        return ItemInfoProductDetailBinding.inflate(
            inflater,
            binding.root,
            false
        ).apply {
            infoDetailTitle.text = catalog.key
            infoDetailValue.text = catalog.value.parseAsHtmlLink(root.context)
            infoDetailValue.maxLines = Int.MAX_VALUE
        }.root
    }

    private fun onImpressView(catalog: ItemCatalog) {
        listener.onImpressCatalog(
            key = catalog.key,
            value = catalog.value,
            position = bindingAdapterPosition + Int.ONE
        )
    }
}
