package com.tokopedia.topads.sdk.v2.tdnbanner.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify
import timber.log.Timber

class SingleTdnView : BaseCustomView {

    private val tdnBanner: ImageView by lazy { findViewById(R.id.tdnBanner) }
    private val tdnShimmer: LoaderUnify by lazy { findViewById(R.id.tdnShimmer) }

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
    }

    fun setTdnModel(
        topAdsImageUiModel: TopAdsImageUiModel,
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        cornerRadius: Int,
        onLoadFailed: () -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
    ) {
        tdnShimmer.layoutParams =
            ConstraintLayout.LayoutParams(
                context.resources.displayMetrics.widthPixels,
                context.resources.getDimensionPixelSize(R.dimen.sdk_dp_112)
            )
        loadImage(
            topAdsImageUiModel,
            onTdnBannerClicked = onTdnBannerClicked,
            cornerRadius = cornerRadius,
            onLoadFailed = onLoadFailed,
            onTdnBannerImpressed = onTdnBannerImpressed
        )
    }

    private fun loadImage(
        imageData: TopAdsImageUiModel,
        cornerRadius: Int = Int.ZERO,
        onLoadFailed: () -> Unit,
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
    ) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            val width = context.resources.displayMetrics.widthPixels

            tdnBanner.loadImage(imageData.imageUrl, properties = {
                if (cornerRadius > Int.ZERO) {
                    transforms(listOf(FitCenter(), RoundedCorners(cornerRadius)))
                } else {
                    fitCenter()
                }

                overrideSize(Resize(width, getHeight(imageData.imageWidth, imageData.imageHeight, width.toFloat())))

                listener(onSuccess = { _, _ ->
                    tdnShimmer.hide()
                    recordImpression(imageData, onTdnBannerImpressed)
                    Timber.d("TDN Banner is loaded successfully")

                    recordClick(imageData, onTdnBannerClicked)
                }, onError = {
                        Timber.d("Error in loading TDN Banner")
                        onLoadFailed.invoke()
                    })
            })
        } else {
            tdnBanner.hide()
        }
    }

    private fun recordClick(
        imageData: TopAdsImageUiModel,
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit
    ) {
        tdnBanner.setOnClickListener {
            Timber.d("TDN Banner is clicked")
            imageData.applink?.let { applink -> onTdnBannerClicked(imageData) }
            TopAdsUrlHitter(context).hitClickUrl(
                this@SingleTdnView.javaClass.canonicalName,
                imageData.adClickUrl,
                "",
                "",
                ""
            )
        }
    }

    private fun recordImpression(
        imageData: TopAdsImageUiModel,
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
    ) {
        imageData.ImpressHolder?.let { ImpressHolder ->
            tdnBanner.addOnImpressionListener(ImpressHolder) {
                TopAdsUrlHitter(context).hitImpressionUrl(
                    this.javaClass.name,
                    imageData.adViewUrl,
                    "",
                    "",
                    ""
                )
                onTdnBannerImpressed.invoke(imageData)
            }
        }
    }

    private fun getHeight(width: Int, height: Int, deviceWidth: Float): Int {
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }
}
