package com.tokopedia.product.detail.view.fragment.productview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.text.util.Linkify
import android.view.View
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.Category
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.Video
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.util.successRate
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.partial_product_full_descr.view.*

class PartialProductDescrFullView private constructor(private val view: View,
                                                      private val activity: Activity? = null){

    var shopInfo: ShopInfo? = null

    companion object {
        private const val MAX_CHAR = 300
        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"
        fun build(_view: View, _activity: Activity?) = PartialProductDescrFullView(_view, _activity)
    }

    init {
        view.youtube_scroll.layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.HORIZONTAL, false)
        view.youtube_scroll.addItemDecoration(SpaceItemDecoration(view.context.resources.getDimensionPixelSize(R.dimen.dp_16),
                LinearLayoutManager.HORIZONTAL))
    }

    fun renderData(data: ProductInfo){
        with(view){
            if (data.videos.isNotEmpty()) {
                youtube_scroll.adapter = YoutubeThumbnailAdapter(data.videos.toMutableList()){
                    _, index -> gotoVideoPlayer(data.videos, index)
                }
                youtube_scroll.visibility = View.VISIBLE
            } else {
                youtube_scroll.visibility = View.GONE
            }

            txt_weight.text = context.getString(R.string.template_weight, data.basic.weight,
                    data.basic.weightUnit.toString())
            txt_success_rate.text = String.format("%.2f%%", data.txStats.successRate)

            label_asuransi.visible()
            txt_asuransi.visible()
            txt_asuransi.text = if (data.basic.isMustInsurance) "Ya" else "Opsional"

            if (data.menu.name.isNotBlank()){
                txt_etalase.text = MethodChecker.fromHtml(data.menu.name)
                txt_etalase.visible()
                label_etalase.visible()
                txt_etalase.setOnClickListener {
                    gotoEtalase(data.menu.id, data.basic.shopID)
                }
            } else {
                txt_etalase.gone()
                label_etalase.gone()
            }

            if (data.category.detail.isNotEmpty()){
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

            if (data.preorder.isActive){
                txt_pre_order.text = context.getString(R.string.template_preorder_time, data.preorder.duration)
                label_pre_order.visibility = View.VISIBLE
                txt_pre_order.visibility = View.VISIBLE
            } else {
                label_pre_order.visibility = View.GONE
                txt_pre_order.visibility = View.GONE
            }

            txt_min_order.text = context.getString(R.string.template_min_order, data.basic.minOrder)
            txt_product_condition.text = if (data.basic.condition == 1) "Baru" else "Bekas"

            val descFormatted = MethodChecker.fromHtml(if (data.basic.description.isNotBlank()) data.basic.description
                else NO_DESCRIPTION)

            txt_product_descr.text = if (descFormatted.toString().length > MAX_CHAR){
                MethodChecker.fromHtml(descFormatted.toString().replace("(\r\n|\n)".toRegex(), "<br />") + "....")
            } else descFormatted

            txt_product_descr.autoLinkMask = 0
            Linkify.addLinks(txt_product_descr, Linkify.WEB_URLS)

            label_see_detail_product_descr.setOnClickListener {
                view.context.startActivity(ProductFullDescriptionActivity.createIntent(view.context,
                        data.basic.name, data.basic.price, shopInfo?.shopCore?.name ?: "",
                        data.pictures[0].urlThumbnail,
                        data.basic.description, data.videos.map { it.url },
                        shopInfo?.goldOS?.isOfficial == 1))
                activity?.overridePendingTransition(R.anim.pull_up, 0)
            }
        }
    }

    private fun openCategory(category: Category.Detail) {
        if (!GlobalConfig.isSellerApp() && view.context.applicationContext is ProductDetailRouter){
            view.context.startActivity((view.context.applicationContext as ProductDetailRouter)
                    .getIntermediaryIntent(view.context, category.id))
        }
    }

    private fun gotoEtalase(etalaseId: Int, shopID: Int) {
        val appContext = view.context.applicationContext
        if (appContext !is ProductDetailRouter)
            return

        val intent = if (etalaseId == 0){
            appContext.getShoProductListIntent(view.context, shopID.toString(), "", etalaseId.toString())
        } else {
            appContext.getShopPageIntent(view.context, shopID.toString())
        }
        view.context.startActivity(intent)
    }

    private fun gotoVideoPlayer(videos: List<Video>, index: Int) {
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.context.applicationContext)
                == YouTubeInitializationResult.SUCCESS){
            view.context.startActivity(ProductYoutubePlayerActivity.createIntent(view.context, videos.map { it.url }, index))
        } else {
            view.context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videos[index].url)));
        }
    }
}