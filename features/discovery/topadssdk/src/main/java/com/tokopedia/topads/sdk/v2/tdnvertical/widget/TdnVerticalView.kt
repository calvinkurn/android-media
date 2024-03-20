package com.tokopedia.topads.sdk.v2.tdnvertical.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.presentation.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.tdnvertical.listener.TdnVerticalBannerResponseListener
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

class TdnVerticalView : BaseCustomView {

    private val tdnBanner: ImageView by lazy { findViewById(R.id.tdnVerticalBanner) }
    private val tdnShimmer: LoaderUnify by lazy { findViewById(R.id.tdnVerticalShimmer) }
    private var tdnVerticalBannerResponseListener: TdnVerticalBannerResponseListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        var counter = 0
    }

    private val topAdsImageViewViewModel by lazy {
        (context as? AppCompatActivity)?.let {
            ViewModelProvider(
                it,
                viewModelFactory
            )[
                (
                    TopAdsImageViewViewModel::class.java.canonicalName
                        ?: ""
                    ) + counter++, TopAdsImageViewViewModel::class.java
            ]
        }
    }

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

    init {
        initInjector()
        observeTopAdsImageView()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_widget_vertical_tdn_view, this)
    }

    private fun initInjector() {
        val application = context.applicationContext as BaseMainApplication
        val component = DaggerTopAdsComponent.builder()
            .baseAppComponent(application.baseAppComponent)
            .build()
        component.inject(this)
    }

    private fun observeTopAdsImageView() {
        (context as? LifecycleOwner)?.let { lifeCycleOwner ->
            topAdsImageViewViewModel?.getResponse()?.observe(lifeCycleOwner) {
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
        val qp = topAdsImageViewViewModel?.getQueryParams(
            query,
            source,
            pageToken,
            adsCount,
            dimenId,
            depId,
            productID,
            page
        )
        qp?.let { topAdsImageViewViewModel?.getImageData(it) }
    }

    fun renderTdnBanner(
        tdnBanners: List<TopAdsImageUiModel>,
        cornerRadius: Int = 8.toPx(),
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        onLoadFailed: () -> Unit = {},
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit = {}
    ) {
        if (tdnBanners.isNotEmpty()) {
            this.show()
            setTdnModel(tdnBanners.first(), onTdnBannerClicked, cornerRadius, onLoadFailed, onTdnBannerImpressed)
        } else {
            this.hide()
        }
    }

    private fun setTdnModel(
        topAdsImageViewModel: TopAdsImageUiModel,
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        cornerRadius: Int,
        onLoadFailed: () -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
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
        imageData: TopAdsImageUiModel,
        cornerRadius: Int = Int.ZERO,
        onLoadFailed: () -> Unit,
        onTdnBannerClicked: (imageData: TopAdsImageUiModel) -> Unit,
        onTdnBannerImpressed: (imageData: TopAdsImageUiModel) -> Unit
    ) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            tdnBanner.loadImage(imageData.imageUrl) {
                transforms(listOf(FitCenter(), RoundedCorners(cornerRadius)))
                listener(
                    onSuccess = { _, _ ->
                        tdnShimmer.hide()
                        recordImpression(imageData, onTdnBannerImpressed)
                        Timber.d("TDN Banner is loaded successfully")
                        recordClick(imageData, onTdnBannerClicked)
                    },
                    onError = { _ ->
                        Timber.d("Error in loading TDN Banner")
                        onLoadFailed.invoke()
                    }
                )
            }
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
                this@TdnVerticalView.javaClass.canonicalName,
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
}
