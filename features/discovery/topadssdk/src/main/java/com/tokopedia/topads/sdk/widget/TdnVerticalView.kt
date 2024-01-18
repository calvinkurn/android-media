package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnVerticalBannerResponseListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class TdnVerticalView : BaseCustomView {

    private val tdnBanner: ImageView by lazy { findViewById(R.id.tdnVerticalBanner) }
    private val tdnShimmer: LoaderUnify by lazy { findViewById(R.id.tdnVerticalShimmer) }
    private var tdnVerticalBannerResponseListener: TdnVerticalBannerResponseListener? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @JvmField
    @Inject
    var topAdsImageViewViewModel: WeakReference<TopAdsImageViewViewModel>? = null

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
        View.inflate(context, R.layout.layout_widget_vertical_tdn_view, this)
        val application = context.applicationContext as BaseMainApplication
        val component = DaggerTopAdsComponent.builder()
            .baseAppComponent(application.baseAppComponent)
            .build()
        component.inject(this)

    }

    fun setTdnResponseListener(listener: TdnVerticalBannerResponseListener) {
        tdnVerticalBannerResponseListener = listener
    }

    fun getTdnData(
        source: String,
        adsCount: Int,
        dimenId: Int,
        query: String = "",
        depId: String = "",
        pageToken: String = "",
        productID: String = "",
        page: String = ""
    ) {
        val qp = topAdsImageViewViewModel?.get()?.getQueryParams(
            query,
            source,
            pageToken,
            adsCount,
            dimenId,
            depId,
            productID,
            page
        )
        qp?.let { topAdsImageViewViewModel?.get()?.getImageData(it) }

        topAdsImageViewViewModel?.get()?.getResponse()?.observe(context as LifecycleOwner) {
            when (it) {
                is Success -> {
                    tdnVerticalBannerResponseListener?.onTdnVerticalBannerResponse(it.data)
                    Timber.d("Response received successfully")
                }

                is Fail -> {
                    Timber.d("error in response")
                }
            }

        }
    }

    fun renderTdnBanner(
        tdnBanners: List<TopAdsImageViewModel>,
        cornerRadius: Int = 8.toPx(),
        onTdnBannerClicked: (imageData: TopAdsImageViewModel) -> Unit,
        onLoadFailed: () -> Unit = {},
        onTdnBannerImpressed: (imageData: TopAdsImageViewModel) -> Unit = {}
    ) {
        setTdnModel(tdnBanners.first(), onTdnBannerClicked, cornerRadius, onLoadFailed, onTdnBannerImpressed)
    }

    private fun setTdnModel(
        topAdsImageViewModel: TopAdsImageViewModel,
        onTdnBannerClicked: (imageData: TopAdsImageViewModel) -> Unit,
        cornerRadius: Int,
        onLoadFailed: () -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageViewModel) -> Unit
    ) {
        loadImage(
            topAdsImageViewModel,
            onTdnBannerClicked = onTdnBannerClicked,
            cornerRadius = cornerRadius,
            onLoadFailed = onLoadFailed,
            onTdnBannerImpressed = onTdnBannerImpressed
        )
    }

    private fun loadImage(
        imageData: TopAdsImageViewModel,
        cornerRadius: Int = Int.ZERO,
        onLoadFailed: () -> Unit,
        onTdnBannerClicked: (imageData: TopAdsImageViewModel) -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageViewModel) -> Unit
    ) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            Glide.with(context).load(imageData.imageUrl)
                .transform(FitCenter(), RoundedCorners(cornerRadius))
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
                        tdnShimmer.hide()
                        recordImpression(imageData, onTdnBannerImpressed)
                        Timber.d("TDN Banner is loaded successfully")

                        recordClick(imageData, onTdnBannerClicked)

                        return false
                    }

                })
                .into(tdnBanner)

        } else {
            tdnBanner.hide()
        }

    }

    private fun recordClick(
        imageData: TopAdsImageViewModel,
        onTdnBannerClicked: (imageData: TopAdsImageViewModel) -> Unit
    ) {
        tdnBanner.setOnClickListener {
            Timber.d("TDN Banner is clicked")
            imageData.applink?.let { applink -> onTdnBannerClicked(imageData) }
            TopAdsUrlHitter(context).hitClickUrl(
                this@TdnVerticalView.javaClass.canonicalName,
                imageData.adClickUrl,
                "",
                "",
                ""
            )
        }
    }

    private fun recordImpression(
        imageData: TopAdsImageViewModel,
        onTdnBannerImpressed: (imageData: TopAdsImageViewModel) -> Unit
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
}
