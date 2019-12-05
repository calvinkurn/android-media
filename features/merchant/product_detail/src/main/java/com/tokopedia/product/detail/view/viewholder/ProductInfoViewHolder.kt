package com.tokopedia.product.detail.view.viewholder

import android.text.util.Linkify
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoData
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.data.model.datamodel.ProductInfoDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.view.adapter.ProductInfoAdapter
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_info.view.*

class ProductInfoViewHolder(private val view: View,
                            private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_info
        private const val MAX_CHAR = 300
        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"
    }

    override fun bind(element: ProductInfoDataModel) {
        element.data?.let { data ->
            view.rv_info.apply {
                val topData = data.find { it.row == "top" } ?: return@apply
                val lastCategory = element.dynamicProductInfoP1?.basic?.category?.detail?.lastOrNull()
                        ?: Category.Detail()

                adapter = ProductInfoAdapter(topData.listOfContent, ProductInfoData(element.dynamicProductInfoP1?.basic?.menu?.id
                        ?: "", element.dynamicProductInfoP1?.basic?.shopID
                        ?: "", lastCategory.id), listener)

                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            val bottomData = data.find { it.row == "bottom" } ?: return
            renderDescriptionData(bottomData.listOfContent, element.shopName, element.dynamicProductInfoP1 ?: DynamicProductInfoP1(),
                    element.productSpecification ?: ProductSpecificationResponse())
        }
    }

    private fun renderDescriptionData(listOfData: List<Content>, shopName: String, infoData: DynamicProductInfoP1, productSpecificationResponse: ProductSpecificationResponse) {
        with(itemView.base_info_and_description) {
            val productInfo = infoData
            if (productInfo.data.videos.isNotEmpty()) {
                view.youtube_scroll.visible()
                view.youtube_scroll.adapter = YoutubeThumbnailAdapter(productInfo.data.videos.toMutableList()) { _, index ->
                    productInfo.data.videos.run { listener.gotoVideoPlayer(this, index) }
                }
                view.youtube_scroll.adapter?.notifyDataSetChanged()
            } else {
                view.youtube_scroll.gone()
            }

            val descFormatted = MethodChecker.fromHtmlPreserveLineBreak(if (listOfData.first().subtitle.isNotBlank()) listOfData.first().subtitle
            else NO_DESCRIPTION)

            txt_product_descr.text = if (descFormatted.length > MAX_CHAR) {
                val subDescr = descFormatted.toString().substring(0, MAX_CHAR)
                MethodChecker.fromHtml(subDescr.replace("(\r\n|\n)".toRegex(), "<br />") + "....")
            } else descFormatted

            txt_product_descr.autoLinkMask = 0
            Linkify.addLinks(txt_product_descr, Linkify.WEB_URLS)

            label_see_detail_product_descr.setOnClickListener {
                listener.gotoDescriptionTab(
                        DescriptionData(
                                basicId = productInfo.basic.productID,
                                basicName = productInfo.getProductName,
                                basicPrice = productInfo.data.price.value.toFloat(),
                                shopName = shopName,
                                thumbnailPicture = productInfo.data.getFirstProductImage() ?: "",
                                basicDescription = listOfData.first().subtitle,
                                videoUrlList = productInfo.data.videos.map { it.url },
                                isOfficial = productInfo.data.isOS),

                        productSpecificationResponse.productCatalogQuery.data.catalog.specification

                )

            }
            visible()
        }
    }

}