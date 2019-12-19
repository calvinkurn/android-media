package com.tokopedia.product.detail.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_product_full_description.*

class ProductFullDescriptionFragment : BaseDaggerFragment() {

    companion object {
        private const val PARAM_DESCRIPTION_DATA = "description_data"
        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"

        fun createInstance(descriptionData: DescriptionData) =
                ProductFullDescriptionFragment().apply {
                    arguments = Bundle().also {
                        it.putParcelable(PARAM_DESCRIPTION_DATA, descriptionData)
                    }
                }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_full_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        youtube_scroll.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        youtube_scroll.addItemDecoration(SpaceItemDecoration(context?.resources?.getDimensionPixelSize(R.dimen.dp_16)
                ?: 0,
                LinearLayoutManager.HORIZONTAL))
        arguments?.let {
            val descriptionData: DescriptionData = it.getParcelable(PARAM_DESCRIPTION_DATA)
                    ?: DescriptionData()
            ImageHandler.loadImageAndCache(product_image, descriptionData.thumbnailPicture)
            product_name.text = MethodChecker.fromHtml(descriptionData.basicName)
            product_price.text = descriptionData.basicPrice.getCurrencyFormatted()
            product_shop.text = descriptionData.shopName
            if (descriptionData.isOfficial) {
                product_shop.setDrawableLeft(R.drawable.ic_official_store_product)
            } else {
                product_shop.setDrawableLeft(-1)
            }
            val vids = descriptionData.videoUrlList.map { Video(url = it) }
            if (vids.size > 0) {
                youtube_scroll.adapter = YoutubeThumbnailAdapter(vids.toMutableList()) { _, index ->
                    gotoVideoPlayer(vids, index)
                }
                youtube_scroll.visible()
            } else {
                youtube_scroll.gone()
            }

            val descr = descriptionData.basicDescription
            val descFormatted = MethodChecker.fromHtml(if (descr.isNotBlank())
                descr.replace("(\r\n|\n)".toRegex(), "<br />") else NO_DESCRIPTION)

            txt_product_descr.text = descFormatted

            txt_product_descr.autoLinkMask = 0
            Linkify.addLinks(txt_product_descr, Linkify.WEB_URLS)
        }
    }

    private fun gotoVideoPlayer(vids: List<Video>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, vids.map { it.url }, index))
            } else {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + vids[index].url)));
            }
        }
    }
}
