package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogImageViewAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import kotlinx.android.synthetic.main.item_catalog_product_info.view.*

class CatalogInfoViewHolder (private val view : View,
                             private val listener : CatalogDetailListener) : AbstractViewHolder<CatalogInfoDataModel>(view){

    private var imageViewAdapter : CatalogImageViewAdapter? = null
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_product_info
    }

    override fun bind(element: CatalogInfoDataModel) {
        renderProductHeaderInfo(element)
    }

    private fun renderProductHeaderInfo(productInfo: CatalogInfoDataModel) {
        val imagesRV = view.findViewById<RecyclerView>(R.id.catalog_images_rv)
        imagesRV.layoutManager = layoutManager
        imageViewAdapter = CatalogImageViewAdapter(productInfo.images, listener)
        imagesRV.adapter = imageViewAdapter!!

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