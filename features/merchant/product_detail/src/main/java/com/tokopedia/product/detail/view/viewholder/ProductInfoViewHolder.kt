package com.tokopedia.product.detail.view.viewholder

import android.text.util.Linkify
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.datamodel.ProductInfoDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.partial_product_full_descr.view.*

class ProductInfoViewHolder(private val view: View,
                            private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_full_descr

        private const val MAX_CHAR = 300
        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"
    }

    override fun bind(element: ProductInfoDataModel) {
        renderData(element.productInfo ?: ProductInfo(), element.shopInfo ?: ShopInfo(),
                element.productSpecification ?: ProductSpecificationResponse())
    }

    private fun renderData(data: ProductInfo, shopInfo: ShopInfo, productSpecificationResponse: ProductSpecificationResponse) {
        with(itemView.base_info_and_description) {
            val productInfo = data
            if (productInfo.videos.isNotEmpty()) {
                view.youtube_scroll.visible()
                view.youtube_scroll.adapter = YoutubeThumbnailAdapter(productInfo.videos.toMutableList()) { _, index ->
                    productInfo.videos.run { listener.gotoVideoPlayer(this, index) }
                }
                view.youtube_scroll.adapter?.notifyDataSetChanged()
            } else {
                view.youtube_scroll.gone()
            }

            txt_weight.text = context.getString(R.string.template_weight, data.basic.weight.numberFormatted(),
                    if (data.basic.weightUnit.toLowerCase() == KG) LABEL_KG else LABEL_GRAM)

            label_asuransi.visible()
            txt_asuransi.visible()
            txt_asuransi.text = if (data.basic.isMustInsurance) "Ya" else "Opsional"

            with(txt_etalase) {
                text = if (data.menu.name.isNotBlank()) {
                    MethodChecker.fromHtml(data.menu.name)
                } else {
                    context.getString(R.string.pdp_etalase_empty_default_value)
                }
                setOnClickListener {
                    listener.gotoEtalase(data.menu.id, data.basic.shopID)
                }
            }

            if (data.category.detail.isNotEmpty()) {
                txt_category.text = MethodChecker.fromHtml(data.category.detail.last().name)
                txt_category.setOnClickListener {
                    listener.openCategory(data.category.detail.last())
                }
                txt_category.visible()
                label_category.visible()
            } else {
                txt_category.gone()
                label_category.gone()
            }

            if (data.preorder.isActive) {
                txt_pre_order.text = context.getString(R.string.template_preorder_time, data.preorder.duration,
                        data.preorder.timeUnitValue)
                label_pre_order.visibility = View.VISIBLE
                txt_pre_order.visibility = View.VISIBLE
            } else {
                label_pre_order.visibility = View.GONE
                txt_pre_order.visibility = View.GONE
            }

            txt_min_order.text = context.getString(R.string.template_min_order, data.basic.minOrder)
            txt_product_condition.text = if (data.basic.condition == ProductConditionTypeDef.NEW) "Baru" else "Bekas"

            val descFormatted = MethodChecker.fromHtmlPreserveLineBreak(if (data.basic.description.isNotBlank()) data.basic.description
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
                                basicId = data.basic.id.toString(),
                                basicName = data.basic.name,
                                basicPrice = data.basic.price,
                                shopName = shopInfo.shopCore.name,
                                thumbnailPicture = data.firstThumbnailPicture,
                                basicDescription = data.basic.description,
                                videoUrlList = data.videos.map { it.url },
                                isOfficial = shopInfo.goldOS.isOfficial == 1),

                        productSpecificationResponse.productCatalogQuery.data.catalog.specification

                )

            }
            visible()
        }
    }

}