package com.tokopedia.product.detail.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_product_full_description.*

class ProductFullDescriptionFragment: BaseDaggerFragment() {

    companion object {
        private const val PARAM_PRODUCT_NAME = "product_name"
        private const val PARAM_PRODUCT_PRICE = "product_price"
        private const val PARAM_PRODUCT_SHOP = "product_shop"
        private const val PARAM_PRODUCT_IMAGE = "product_image"
        private const val PARAM_PRODUCT_DESCR = "product_descr"
        private const val PARAM_PRODUCT_VIDS = "product_vids"
        private const val PARAM_IS_OS = "is_os"

        private const val NO_DESCRIPTION = "TIDAK ADA DESKRIPSI"

        fun createInstance(productName: String, productPrice: Float, shopName: String,
                           productImage: String, productDescr: String, productVids: List<String>,
                           isOS: Boolean = false) =
                ProductFullDescriptionFragment().apply {
                    arguments = Bundle().also {
                        it.putString(PARAM_PRODUCT_NAME, productName)
                        it.putFloat(PARAM_PRODUCT_PRICE, productPrice)
                        it.putString(PARAM_PRODUCT_SHOP, shopName)
                        it.putString(PARAM_PRODUCT_IMAGE, productImage)
                        it.putString(PARAM_PRODUCT_DESCR, productDescr)
                        it.putStringArray(PARAM_PRODUCT_VIDS, productVids.toTypedArray())
                        it.putBoolean(PARAM_IS_OS, isOS)
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
            ImageHandler.loadImageAndCache(product_image, it.getString(PARAM_PRODUCT_IMAGE))
            product_name.text = MethodChecker.fromHtml(it.getString(PARAM_PRODUCT_NAME))
            product_price.text = it.getFloat(PARAM_PRODUCT_PRICE).getCurrencyFormatted()
            product_shop.text = it.getString(PARAM_PRODUCT_SHOP)
            if (it.getBoolean(PARAM_IS_OS, false)) {
                product_shop.setDrawableLeft(R.drawable.ic_official_store_product)
            } else {
                product_shop.setDrawableLeft(-1)
            }
            val vids = (it.getStringArray(PARAM_PRODUCT_VIDS) ?: arrayOf()).map { Video(url = it) }
            if (vids.size > 0) {
                youtube_scroll.adapter = YoutubeThumbnailAdapter(vids.toMutableList()) { _, index ->
                    gotoVideoPlayer(vids, index)
                }
                youtube_scroll.visible()
            } else {
                youtube_scroll.gone()
            }

            val descr = it.getString(PARAM_PRODUCT_DESCR, "")
            val descFormatted = MethodChecker.fromHtml(if (descr.isNotBlank()) descr else NO_DESCRIPTION)

            txt_product_descr.text =  descFormatted

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
                        Uri.parse("http://www.youtube.com/watch?v=" + vids[index].url)));
            }
        }
    }
}
