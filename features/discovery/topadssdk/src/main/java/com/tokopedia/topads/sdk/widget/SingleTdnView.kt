package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.BaseCustomView
import timber.log.Timber

class SingleTdnView : BaseCustomView {

    private var tdnBanner: ImageView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_widget_single_tdn_view, this)
        tdnBanner = findViewById(R.id.tdnBanner)
    }

    fun setTdnModel(
        topAdsImageViewModel: TopAdsImageViewModel,
        onTdnBannerClicked: (applink: String) -> Unit,
        cornerRadius: Int,
    ) {
        tdnBanner?.let {
            loadImage(
                topAdsImageViewModel,
                onTdnBannerClicked = onTdnBannerClicked,
                cornerRadius = cornerRadius
            )
        }
    }

    fun loadImage(
        imageData: TopAdsImageViewModel,
        cornerRadius: Int = Int.ZERO,
        onLoadFailed: () -> Unit = {},
        onTdnBannerClicked: (applink: String) -> Unit,
    ) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            val width = context.resources.displayMetrics.widthPixels
            tdnBanner?.let {
                getRequestBuilder(imageData.imageUrl, cornerRadius).override(
                    width,
                    getHeight(imageData.imageWidth, imageData.imageHeight, width)
                )
                    .addListener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.d("Error in loading TDN Banner")
                            onLoadFailed.invoke()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            recordImpression(imageData)

                            Timber.d("TDN Banner is loaded successfully")

                            recordClick(imageData, onTdnBannerClicked)

                            return false
                        }

                    })
                    .into(it)
            }
        } else {
            tdnBanner?.hide()
        }

    }

    private fun recordClick(
        imageData: TopAdsImageViewModel,
        onTdnBannerClicked: (applink: String) -> Unit
    ) {
        tdnBanner?.setOnClickListener {
            Timber.d("TDN Banner is clicked")
            imageData.applink?.let { applink -> onTdnBannerClicked(applink) }
            TopAdsUrlHitter(context).hitClickUrl(
                this@SingleTdnView.javaClass.canonicalName,
                imageData.adClickUrl,
                "",
                "",
                ""
            )
        }
    }

    private fun recordImpression(imageData: TopAdsImageViewModel) {
        imageData.ImpressHolder?.let { ImpressHolder ->
            tdnBanner?.addOnImpressionListener(ImpressHolder) {
                TopAdsUrlHitter(context).hitImpressionUrl(
                    this.javaClass.name,
                    imageData.adViewUrl,
                    "",
                    "",
                    ""
                )
            }
        }
    }

    private fun getRequestBuilder(imageUrl: String?, radius: Int): RequestBuilder<Drawable> {
        return if (radius > Int.ZERO) {
            Glide.with(context)
                .load(imageUrl)
                .transform(FitCenter(), RoundedCorners(radius))
        } else {
            Glide.with(context)
                .load(imageUrl)
                .fitCenter()

        }
    }

    private fun getHeight(width: Int, height: Int, deviceWidth: Int): Int {
        val deviceWidth = deviceWidth.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }
}
