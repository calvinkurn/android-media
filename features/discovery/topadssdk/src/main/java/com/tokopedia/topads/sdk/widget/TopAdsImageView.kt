package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.di.TopAdsComponent
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopAdsImageView : AppCompatImageView, HasComponent<TopAdsComponent> {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var topAdsImageViewViewModel: TopAdsImageViewViewModel

    private var topAdsImageViewClickListener: TopAdsImageViewClickListener? = null
    private var topAdsImageViewImpressionListener: TopAdsImageViewImpressionListener? = null
    private var topAdsImageVieWApiResponseListener: TopAdsImageVieWApiResponseListener? = null
    private val className: String = "com.tokopedia.topads.sdk.widget.TopAdsImageView"

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
        val activity: FragmentActivity by lazy {
            try {
                context as FragmentActivity
            } catch (exception: ClassCastException) {
                throw ClassCastException("Please ensure that the provided Context is a valid FragmentActivity")
            }
        }
        topAdsImageViewViewModel = ViewModelProviders
                .of(activity, viewModelProvider)
                .get(TopAdsImageViewViewModel::class.java)
    }

    /**
     * Use this function to get callback when user click on view
     * @param listener pass implementation of listener
     * */
    fun setTopAdsImageViewClick(listener: TopAdsImageViewClickListener) {
        topAdsImageViewClickListener = listener
    }

    /**
     * Use this function to get callback when view is visible on screen
     * @param listener pass implementation of listener
     * */
    fun setTopAdsImageViewImpression(listener: TopAdsImageViewImpressionListener) {
        topAdsImageViewImpressionListener = listener
    }


    /** Use this function to hit the api according to parameters, and handle response in callbacks
     * @param query Search term query to look for ads. Example: leather shoes
     * @param source Page source that do the request. Example: search, fav_product
     * @param pageToken Use in pagination, pass empty in case of first page
     * @param adsCount Use this parameter, To tell number of ads on page required
     * @param dimenId Use this parameter to provide dimen id
     * @param depId Required in case of category and intermediate page else optional
     * */
    fun getImageData(query: String, source: String, adsCount: String, dimenId: Int, depId: String = "", pageToken: String = "") {
        val queryParams = topAdsImageViewViewModel.getQueryParams(query,source, pageToken, adsCount, dimenId, depId)
        topAdsImageViewViewModel.getImageData(queryParams)

        topAdsImageViewViewModel.getResponse().observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    topAdsImageVieWApiResponseListener?.onImageViewResponse(it.data)
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

    /**
     * Use this function to get callbacks of response and error
     * @param listener pass implementation of listener
     * */
    fun setApiResponseListener(listener: TopAdsImageVieWApiResponseListener) {
        topAdsImageVieWApiResponseListener = listener
    }

    /**
     * Use this function to load the ads by passing parameter, also handle tracking of clicks and impression
     * If imageUrl is null or empty view will hide itself
     * @param imageData The object of TopAdsViewModel
     * */
    fun loadImage(imageData: TopAdsImageViewModel) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            ImageHandler.LoadImageResize(context,
                    this,
                    imageData.imageUrl,
                    context.resources.displayMetrics.widthPixels,
                    getHeight(imageData.imageWidth, imageData.imageHeight)
            )
            topAdsImageViewImpressionListener?.onTopAdsImageViewImpression(imageData.adViewUrl
                    ?: "")
            this.setOnClickListener {
                topAdsImageViewClickListener?.onTopAdsImageViewClicked(imageData.applink)
                ImpresionTask(className).execute(imageData.adClickUrl)
            }
        } else {
            this.hide()
        }

    }

    private fun getHeight(width: Int, height: Int): Int {
        val metrics = context.resources.displayMetrics
        val deviceWidth = metrics.widthPixels.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        topAdsImageViewViewModel.onClear()
    }

}