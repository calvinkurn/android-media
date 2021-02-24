package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogImagesAdapter
import com.tokopedia.catalog.adapter.CatalogImagesItemDecoration
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.PageControl
import kotlinx.android.synthetic.main.item_catalog_product_info.view.*

class CatalogInfoViewHolder(private val view: View,
                            private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogInfoDataModel>(view) {

    private var imagesAdapter: CatalogImagesAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_catalog_product_info
    }

    override fun bind(element: CatalogInfoDataModel) {
        renderProductHeaderInfo(element)
    }

    private fun renderProductHeaderInfo(productInfo: CatalogInfoDataModel) {
        val imagesRV = view.findViewById<RecyclerView>(R.id.catalog_images_rv)
        val imagesPageControl = view.findViewById<PageControl>(R.id.catalog_images_page_control)
        imagesAdapter = CatalogImagesAdapter(productInfo.images, catalogDetailListener)
        imagesPageControl.inactiveColor = ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
        imagesPageControl.setIndicator(productInfo.images.size)
        imagesRV.apply {
            adapter = imagesAdapter
            addItemDecoration(CatalogImagesItemDecoration())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        imagesPageControl.setCurrentIndicator(position)
                    }
                }
            })
        }

        view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.view_more_description).setOnClickListener {
            catalogDetailListener.onViewMoreDescriptionClick()
        }
        view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.product_description_title).setOnClickListener {
            catalogDetailListener.onDescriptionClick()
        }
        productInfo.run {
            view.product_name.text = productName
            view.product_brand.displayTextOrHide(productBrand)
            view.price_range_value.text = priceRange
            view.product_description.text = shortDescription
            if (!tag.isNullOrEmpty()) {
                view.product_tag.show()
                view.product_tag.text = tag
            }
        }
    }
}