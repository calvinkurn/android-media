package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.di.TopAdsComponent
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewResponse
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopAdsImageView : AppCompatImageView, HasComponent<TopAdsComponent> {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var topAdsImageViewViewModel: TopAdsImageViewViewModel

    private var topAdsImageViewClickListener: TopAdsImageViewClickListener? = null
    private var topadsImageViewImpressionListener: TopAdsImageViewImpressionListener? = null
    private var topAdsImageVieWApiResponseListener: TopAdsImageVieWApiResponseListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        component.inject(this)
        topAdsImageViewViewModel = ViewModelProviders
                .of(context as FragmentActivity, viewModelProvider)
                .get(TopAdsImageViewViewModel::class.java)


    }

    fun setTopAdsImageViewClick(listener: TopAdsImageViewClickListener) {
        topAdsImageViewClickListener = listener
    }

    fun setTopAdsImageViewImpression(listener: TopAdsImageViewImpressionListener) {
        topadsImageViewImpressionListener = listener
    }


    fun getImageData(query: String, source: String, pageToken: String, adsCount: Int, dimens: String) {
        val queryParams = topAdsImageViewViewModel.getQueryParams(source, pageToken, adsCount, dimens)
        topAdsImageViewViewModel.getImageData(query, queryParams)

        topAdsImageViewViewModel.getResponse().observe(context as AppCompatActivity, Observer {
            when (it) {
                is Success -> {
                    topAdsImageVieWApiResponseListener?.onImageViewResponse(it.data.data?.get(0)?.banner?.images,"")
                }
                is Fail -> {
                    topAdsImageVieWApiResponseListener?.onError(it.throwable)
                }
            }

        })
    }

    override fun getComponent(): TopAdsComponent {
        return DaggerTopAdsComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    fun setApiResponseListener(listener: TopAdsImageVieWApiResponseListener) {
        topAdsImageVieWApiResponseListener = listener
    }

    fun loadImage(imageUrl: TopAdsImageViewResponse.Data.Banner.Image?) {
        if (!imageUrl?.fullEcs.isNullOrEmpty()) {
            ImageHandler.LoadImageResize(context,
                    this,
                    imageUrl?.fullEcs,
                    400,
                    200
            )
            topadsImageViewImpressionListener?.onTopAdsImageViewImpression(imageUrl?.fullEcs ?: "")
            this.setOnClickListener {
                topAdsImageViewClickListener?.onTopAdsImageViewClicked("applink")
            }
        } else {
            this.hide()
        }

    }


}