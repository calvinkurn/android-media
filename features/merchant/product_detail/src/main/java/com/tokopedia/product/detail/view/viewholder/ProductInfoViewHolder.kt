package com.tokopedia.product.detail.view.viewholder

import android.text.util.Linkify
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductInfoDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.data.util.ProductCustomMovementMethod
import com.tokopedia.product.detail.view.adapter.ProductInfoAdapter
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_product_full_description.*
import kotlinx.android.synthetic.main.item_dynamic_product_info.view.*

class ProductInfoViewHolder(private val view: View,
                            private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_info
        private const val MAX_CHAR = 300
    }

    override fun bind(element: ProductInfoDataModel) {
        element.data?.let { data ->
            view.rv_info.apply {
                val topData = data.find { it.row == "top" } ?: return@apply

                adapter = ProductInfoAdapter(listener, topData.listOfContent, getComponentTrackData(element))
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }

            val bottomData = data.find { it.row == "bottom" } ?: return
            renderDescriptionData(bottomData.listOfContent, element.shopName, element.dynamicProductInfoP1
                    ?: DynamicProductInfoP1(), getComponentTrackData(element))
        }
    }

    private fun renderDescriptionData(listOfData: List<Content>, shopName: String, infoData: DynamicProductInfoP1, componentTrackData: ComponentTrackDataModel) {
        with(itemView.base_info_and_description) {
            val productInfo = infoData
            if (productInfo.data.videos.isNotEmpty()) {
                view.youtube_scroll.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false)

                if (view.youtube_scroll.itemDecorationCount == 0)
                    view.youtube_scroll.addItemDecoration(SpaceItemDecoration(context?.resources?.getDimensionPixelSize(R.dimen.dp_16)
                            ?: 0, LinearLayoutManager.HORIZONTAL))

                view.youtube_scroll.visible()
                view.youtube_scroll.adapter = YoutubeThumbnailAdapter(productInfo.data.videos.toMutableList()) { _, index ->
                    productInfo.data.videos.run { listener.gotoVideoPlayer(this, index) }
                }
                view.youtube_scroll.adapter?.notifyDataSetChanged()
            } else {
                view.youtube_scroll.gone()
            }

            val descFormatted = MethodChecker.fromHtmlPreserveLineBreak(if (listOfData.firstOrNull()?.subtitle?.isNotBlank() == true) listOfData.first().subtitle
            else context.getString(R.string.label_no_description))

            txt_product_descr.text = if (descFormatted.length > MAX_CHAR) {
                val subDescr = descFormatted.toString().substring(0, MAX_CHAR)
                MethodChecker.fromHtml(subDescr.replace("(\r\n|\n)".toRegex(), "<br />") + "....")
            } else descFormatted

            txt_product_descr.autoLinkMask = 0
            Linkify.addLinks(txt_product_descr, Linkify.WEB_URLS)
            txt_product_descr.movementMethod = ProductCustomMovementMethod(::onBranchClicked)

            label_see_detail_product_descr.setOnClickListener {
                listener.gotoDescriptionTab(
                        DescriptionData(
                                basicId = productInfo.basic.productID,
                                basicName = productInfo.getProductName,
                                basicPrice = productInfo.data.price.value.toFloat(),
                                shopName = shopName,
                                thumbnailPicture = productInfo.data.getFirstProductImage() ?: "",
                                basicDescription = listOfData.firstOrNull()?.subtitle ?: "",
                                videoUrlList = productInfo.data.videos.map { it.url },
                                isOfficial = productInfo.data.isOS),
                        componentTrackData
                )

            }
            visible()
        }
    }

    private fun onBranchClicked(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(view.context, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra(RouteManager.BRANCH, url)
            intent.putExtra(RouteManager.BRANCH_FORCE_NEW_SESSION, true)
            view.context.startActivity(intent)
        }
    }

    private fun getComponentTrackData(element: ProductInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)

}