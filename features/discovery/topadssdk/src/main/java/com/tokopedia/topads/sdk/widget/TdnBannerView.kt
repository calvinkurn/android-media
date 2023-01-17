package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_CAROUSEL
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class TdnBannerView : FrameLayout {

    private var tdnCarouselView: TdnCarouselView? = null
    private var singleTdnView: SingleTdnView? = null
    private var tdnBannerResponseListener: TdnBannerResponseListener? = null

    @JvmField @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val topAdsImageViewViewModel by lazy {
        val vm = viewModelFactory?.let {
            ViewModelProvider(context as AppCompatActivity, it).get(
                TopAdsImageViewViewModel::class.java
            )
        }
        WeakReference(vm)
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

    private fun init() {
        val view = View.inflate(context, R.layout.layout_widget_tdn_view, this)
        tdnCarouselView = view.findViewById(R.id.tdnCarouselView)
        singleTdnView = view.findViewById(R.id.singleTdnView)


        val application = context.applicationContext as BaseMainApplication
        val component = DaggerTopAdsComponent.builder()
            .baseAppComponent(application.baseAppComponent)
            .build()
        component.inject(this)

    }

    fun setTdnResponseListener(listener: TdnBannerResponseListener) {
        tdnBannerResponseListener = listener
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
        val qp = topAdsImageViewViewModel.get()?.getQueryParams(
            query,
            source,
            pageToken,
            adsCount,
            dimenId,
            depId,
            productID,
            page
        )
        qp?.let { topAdsImageViewViewModel.get()?.getImageData(it) }

        topAdsImageViewViewModel.get()?.getResponse()?.observe(context as LifecycleOwner, {
            when (it) {
                is Success -> {
                    val categoriesList = TdnHelper.categoriesTdnBanners(it.data)
                    tdnBannerResponseListener?.onTdnBannerResponse(categoriesList)
                    Timber.d("Response received successfully")
                }
                is Fail -> {
                    Timber.d("error in response")
                }
            }

        })
    }

    fun renderTdnBanner(
        tdnBanners: List<TopAdsImageViewModel>,
        cornerRadius: Int = 8.toPx(),
        onTdnBannerClicked: (String) -> Unit,
        onLoadFailed: () -> Unit = {} ,
        onTdnBannerImpressed: () -> Unit = {}
    ) {
        val layoutType = tdnBanners.firstOrNull()?.layoutType
        if (layoutType == TYPE_CAROUSEL || layoutType == TYPE_VERTICAL_CAROUSEL) {
            tdnCarouselView?.setCarouselModel(tdnBanners, onTdnBannerClicked, cornerRadius, onLoadFailed, onTdnBannerImpressed)
            tdnCarouselView?.show()
            singleTdnView?.hide()
        } else if (layoutType == TYPE_SINGLE || layoutType == "") {
            singleTdnView?.setTdnModel(tdnBanners.first(), onTdnBannerClicked, cornerRadius, onLoadFailed, onTdnBannerImpressed)
            singleTdnView?.show()
            tdnCarouselView?.hide()
        }
    }

}
