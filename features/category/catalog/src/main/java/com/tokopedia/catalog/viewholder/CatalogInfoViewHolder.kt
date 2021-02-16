package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogImagesAdapter
import com.tokopedia.catalog.adapter.CatalogImagesItemDecoration
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import kotlinx.android.synthetic.main.item_catalog_product_info.view.*

class CatalogInfoViewHolder (private val view : View,
                             private val catalogDetailListener : CatalogDetailListener) : AbstractViewHolder<CatalogInfoDataModel>(view){

    private var imagesAdapter : CatalogImagesAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_catalog_product_info
    }

    override fun bind(element: CatalogInfoDataModel) {
        renderProductHeaderInfo(element)
    }

    private fun renderProductHeaderInfo(productInfo: CatalogInfoDataModel) {
        val imagesRV = view.findViewById<RecyclerView>(R.id.catalog_images_rv)
        imagesAdapter = CatalogImagesAdapter(productInfo.images, catalogDetailListener)

        imagesRV.apply {
            layoutManager = layoutManager
            adapter = imagesAdapter
            addItemDecoration(CatalogImagesItemDecoration())
        }
        view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.view_more_description).setOnClickListener {
            catalogDetailListener.onViewMoreDescriptionClick()
        }
        productInfo.run {
            view.product_name.text = productName
            view.product_brand.text = productBrand
            view.price_range_value.text = priceRange
            view.product_description.text = MethodChecker.fromHtml(description)
            if(!tag.isNullOrEmpty()){
                view.product_tag.visibility = View.VISIBLE
                view.product_tag.text = tag
            }
        }
    }
}