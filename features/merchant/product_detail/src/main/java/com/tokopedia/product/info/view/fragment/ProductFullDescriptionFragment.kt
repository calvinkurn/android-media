package com.tokopedia.product.info.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.util.ProductCustomMovementMethod
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.YoutubeThumbnailAdapter
import com.tokopedia.product.detail.view.util.SpaceItemDecoration
import com.tokopedia.product.info.model.description.DescriptionData
import com.tokopedia.product.info.view.ProductFullDescriptionListener
import kotlinx.android.synthetic.main.fragment_product_full_description.*

class ProductFullDescriptionFragment : BaseDaggerFragment(), ProductFullDescriptionListener {

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
        youtube_scroll.addItemDecoration(SpaceItemDecoration(context?.resources?.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
                ?: 0,
                LinearLayoutManager.HORIZONTAL))
        arguments?.let {
            val descriptionData: DescriptionData = it.getParcelable(PARAM_DESCRIPTION_DATA)
                    ?: DescriptionData()
            product_image.loadImage(descriptionData.thumbnailPicture) {
                setCacheStrategy(MediaCacheStrategy.DATA)
            }
            product_name.text = MethodChecker.fromHtml(descriptionData.basicName)
            product_price.text = descriptionData.basicPrice.getCurrencyFormatted()
            product_shop.text = descriptionData.shopName

            when {
                descriptionData.isOfficial -> {
                    ic_badge.show()
                    ic_badge.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_official_store_product))
                }
                descriptionData.isGoldMerchant -> {
                    ic_badge.show()
                    ic_badge.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant))
                }
                else -> {
                    ic_badge.hide()
                }
            }

            val vids = descriptionData.videoUrlList.map { YoutubeVideo(url = it) }
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

            setSelectClickableTextView()
        }
    }

    private fun setSelectClickableTextView(){
        txt_product_descr.autoLinkMask = 0
        Linkify.addLinks(txt_product_descr, Linkify.WEB_URLS)

        val selectable = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.O -> false
            else -> true
        }
        txt_product_descr.setTextIsSelectable(selectable)
        txt_product_descr.movementMethod = ProductCustomMovementMethod(::onBranchClicked)
    }

    private fun gotoVideoPlayer(vids: List<YoutubeVideo>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, vids.map { it.url }, index))
            } else {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + vids[index].url)));
                } catch (e: Throwable) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    override fun onBranchClicked(url: String) {
        if (!GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.CONSUMER_SPLASH_SCREEN)
            intent.putExtra(RouteManager.BRANCH, url)
            intent.putExtra(RouteManager.BRANCH_FORCE_NEW_SESSION, true)
            startActivity(intent)
        }
    }
}
