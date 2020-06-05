package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.di.TopAdsComponent
import com.tokopedia.topads.sdk.listener.ImpressionListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import javax.inject.Inject

class TopAdsImageView : AppCompatImageView, HasComponent<TopAdsComponent> {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var topAdsImageViewViewModel: TopAdsImageViewViewModel

    private lateinit var topAdsImageViewClickListener:TopAdsImageViewClickListener
    private lateinit var topadsImageViewImpressionListener: TopAdsImageViewImpressionListener

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        component.inject(this)
        topAdsImageViewViewModel = ViewModelProviders
                .of(context as FragmentActivity, viewModelProvider)
                .get(TopAdsImageViewViewModel::class.java)


    }

    fun setTopAdsImageViewClick(listener: TopAdsImageViewClickListener ){
        topAdsImageViewClickListener = listener
    }

    fun setTopAdsImageViewImpression(listener: TopAdsImageViewImpressionListener){
        topadsImageViewImpressionListener = listener
    }


    fun renderImage(query:String) {
        topAdsImageViewViewModel.renderImage(query)
        ImageHandler.LoadImageResize(context,
                this,
                "https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2020/2/9/7732831/7732831_e649a20b-611f-4d5f-ab0c-68ec6fbb1b2f.png",
                400,
                200
        )
        topadsImageViewImpressionListener.onTopAdsImageViewImpression("viewUrl")
        this.setOnClickListener {
            topAdsImageViewClickListener.onTopAdsImageViewClicked("applink")
        }

    }

    override fun getComponent(): TopAdsComponent {
       return DaggerTopAdsComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

}