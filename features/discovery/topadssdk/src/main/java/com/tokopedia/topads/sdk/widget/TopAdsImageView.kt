package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber

class TopAdsImageView : AppCompatImageView {

    companion object {
        private var noOfObject = 0
    }

    private lateinit var topAdsImageViewViewModel: TopAdsImageViewViewModel
    private var topAdsImageViewClickListener: TopAdsImageViewClickListener? = null
    private var topAdsImageViewImpressionListener: TopAdsImageViewImpressionListener? = null
    private var topAdsImageVieWApiResponseListener: TopAdsImageVieWApiResponseListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
     * @param productID Required for filtering. Ads service will look for ads related to the product id.
     * */
    fun getImageData(source: String, adsCount: Int, dimenId: Int, query: String = "", depId: String = "", pageToken: String = "", productID: String = "") {
        initViewModel()
        val queryParams = topAdsImageViewViewModel.getQueryParams(query, source, pageToken, adsCount, dimenId, depId, productID)
        topAdsImageViewViewModel.getImageData(queryParams)
        topAdsImageViewViewModel.getResponse().observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    topAdsImageVieWApiResponseListener?.onImageViewResponse(it.data)
                    Timber.d("Response received successfully")
                }
                is Fail -> {
                    topAdsImageVieWApiResponseListener?.onError(it.throwable)
                    Timber.d("error in response")
                }
            }

        })
    }


    private fun initViewModel() {
        val activity: FragmentActivity by lazy {
            try {
                context as FragmentActivity
            } catch (exception: ClassCastException) {
                throw ClassCastException("Please ensure that the provided Context is a valid FragmentActivity")
            }
        }
        topAdsImageViewViewModel = ViewModelProviders
                .of(activity)
                .get((TopAdsImageViewViewModel::class.java.canonicalName
                        ?: "") + noOfObject++, TopAdsImageViewViewModel::class.java)
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
    fun loadImage(imageData: TopAdsImageViewModel, cornerRadius: Int = 0, onLoadFailed: () -> Unit = {}) {
        if (!imageData.imageUrl.isNullOrEmpty()) {
            getRequestBuilder(imageData.imageUrl, cornerRadius).override(context.resources.displayMetrics.widthPixels,
                    getHeight(imageData.imageWidth, imageData.imageHeight))
                    .addListener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Timber.d("Error in loading TopAdsImageView")
                            onLoadFailed.invoke()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            topAdsImageViewImpressionListener?.onTopAdsImageViewImpression(imageData.adViewUrl
                                    ?: "")
                            Timber.d("TopAdsImageView is loaded successfully")

                            this@TopAdsImageView.setOnClickListener {
                                topAdsImageViewClickListener?.onTopAdsImageViewClicked(imageData.applink)
                                Timber.d("TopAdsImageView is clicked")
                                TopAdsUrlHitter(context).hitClickUrl(this@TopAdsImageView.javaClass.canonicalName,imageData.adClickUrl,"","","")
                            }
                            return false
                        }

                    })
                    .into(this)
        } else {
            this.hide()
        }

    }

    private fun getRequestBuilder(imageUrl: String?, radius: Int): RequestBuilder<Drawable> {
        return if (radius > 0) {
            Glide.with(context)
                    .load(imageUrl)
                    .transform(FitCenter(), RoundedCorners(radius))
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .fitCenter()

        }
    }

    private fun getHeight(width: Int, height: Int): Int {
        val metrics = context.resources.displayMetrics
        val deviceWidth = metrics.widthPixels.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }

}