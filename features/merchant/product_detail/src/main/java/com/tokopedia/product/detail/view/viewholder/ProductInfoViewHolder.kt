package com.tokopedia.product.detail.view.viewholder

import android.text.util.Linkify
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductInfoDataModel
import com.tokopedia.product.detail.data.util.ProductCustomMovementMethod
import com.tokopedia.product.detail.view.adapter.ProductInfoAdapter
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.item_dynamic_product_info.view.*

class ProductInfoViewHolder(private val view: View,
                            private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_info
        private const val MAX_CHAR = 300
        private const val TOP_ROW_SPECIFICATION_DATA = "top"
        private const val BOTTOM_ROW_DESCRIPTION_DATA = "bottom"
    }

    override fun bind(element: ProductInfoDataModel) {
        element.data?.let { data ->
            view.rv_info.apply {
                val topData = data.find { it.row == TOP_ROW_SPECIFICATION_DATA } ?: return@apply

                adapter = ProductInfoAdapter(listener, topData.listOfContent, getComponentTrackData(element))
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }

            val bottomData = data.find { it.row == BOTTOM_ROW_DESCRIPTION_DATA } ?: return
            renderDescriptionData(bottomData.listOfContent, element.youtubeVideos, getComponentTrackData(element))
        }
    }

    private fun renderDescriptionData(listOfData: List<Content>, youtubeVideos: List<YoutubeVideo>, componentTrackData: ComponentTrackDataModel) = with(view) {
        if (youtubeVideos.isNotEmpty()) {
            youtube_scroll.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false)

            if (youtube_scroll.itemDecorationCount == 0)
                youtube_scroll.addItemDecoration(SpaceItemDecoration(context?.resources?.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
                        ?: 0, LinearLayoutManager.HORIZONTAL))

            youtube_scroll.show()
            youtube_scroll.adapter = YoutubeThumbnailAdapter(youtubeVideos.toMutableList()) { _, index ->
                listener.gotoVideoPlayer(youtubeVideos, index)
            }
            youtube_scroll.adapter?.notifyDataSetChanged()
        } else {
            youtube_scroll.hide()
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
            listener.gotoDescriptionTab(listOfData.firstOrNull()?.subtitle
                    ?: "", componentTrackData)
        }
        visible()
    }

    private fun onBranchClicked(url: String) =with(view){
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(view.context, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra(RouteManager.BRANCH, url)
            intent.putExtra(RouteManager.BRANCH_FORCE_NEW_SESSION, true)
            context.startActivity(intent)
        }
    }

    private fun getComponentTrackData(element: ProductInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}