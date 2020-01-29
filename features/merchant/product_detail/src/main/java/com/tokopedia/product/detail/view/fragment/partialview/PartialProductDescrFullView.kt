package com.tokopedia.product.detail.view.fragment.partialview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.util.Linkify
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionTabActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.partial_product_full_descr.view.*

class PartialProductDescrFullView private constructor(private val view: View,
                                                      private val activity: Activity? = null,
                                                      private val productDetailTracking: ProductDetailTracking) {

    var shopInfo: ShopInfo? = null
    var productInfo: ProductInfo? = null
    var productSpecificationResponse: ProductSpecificationResponse? = null

    companion object {
        private const val MAX_CHAR = 300
        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"
        fun build(_view: View,
                  _activity: Activity?,
                  _productDetailTracking: ProductDetailTracking
        ) = PartialProductDescrFullView(_view, _activity, _productDetailTracking)
    }

    init {
        view.youtube_scroll.layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.HORIZONTAL, false)
        view.youtube_scroll.addItemDecoration(SpaceItemDecoration(view.context.resources.getDimensionPixelSize(R.dimen.dp_16),
                LinearLayoutManager.HORIZONTAL))
    }

    @SuppressLint("LogNotTimber")
    fun renderData(data: ProductInfo) {
        with(view) {
            productInfo = data
            if (productInfo?.videos?.isNotEmpty() == true) {
                view.youtube_scroll.visible()
                view.youtube_scroll.adapter = YoutubeThumbnailAdapter(productInfo?.videos?.toMutableList()
                        ?: mutableListOf()) { _, index ->
                    productInfo?.videos?.run { gotoVideoPlayer(this, index) }
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
                    gotoEtalase(data.menu.id, data.basic.shopID)
                }
            }

            if (data.category.detail.isNotEmpty()) {
                txt_category.text = MethodChecker.fromHtml(data.category.detail.last().name)
                txt_category.setOnClickListener {
                    openCategory(data.category.detail.last())
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
                view.context.startActivity(ProductFullDescriptionTabActivity.createIntent(view.context,
                        DescriptionData(
                                basicId = data.basic.id.toString(),
                                basicName = data.basic.name,
                                basicPrice = data.basic.price,
                                shopName = shopInfo?.shopCore?.name ?: "",
                                thumbnailPicture = data.firstThumbnailPicture,
                                basicDescription = data.basic.description,
                                videoUrlList = data.videos.map { it.url },
                                isOfficial = shopInfo?.goldOS?.isOfficial == 1),
                        productSpecificationResponse?.productCatalogQuery?.data?.catalog?.specification
                                ?: arrayListOf()))
                activity?.overridePendingTransition(R.anim.pull_up, 0)
                productInfo?.let {
                    productDetailTracking.eventClickProductDescriptionReadMore(it.basic.id.toString())
                }
            }
            visible()
        }
    }

    private fun openCategory(category: Category.Detail) {
        if (GlobalConfig.isCustomerApp()) {
            RouteManager.route(view.context,
                    ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL,
                    category.id)
        }
    }

    private fun gotoEtalase(etalaseId: String, shopID: Int) {
        val intent = RouteManager.getIntent(view.context, if (etalaseId.isNotBlank()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopID.toString(), etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopID.toString())
        })
        view.context.startActivity(intent)
    }

    private fun gotoVideoPlayer(videos: List<Video>, index: Int) {
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.context.applicationContext)
                == YouTubeInitializationResult.SUCCESS) {
            view.context.startActivity(ProductYoutubePlayerActivity.createIntent(view.context, videos.map { it.url }, index))
        } else {
            view.context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + videos[index].url)));
        }
    }
}