package com.tokopedia.product.detail.view.fragment.productView

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.text.util.Linkify
import android.view.View
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.Video
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.partial_product_full_descr.view.*

class PartialProductDescrFullView private constructor(private val view: View){
     companion object {
         private const val MAX_CHAR = 300
         private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"
         fun build(_view: View) = PartialProductDescrFullView(_view)
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
        }
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