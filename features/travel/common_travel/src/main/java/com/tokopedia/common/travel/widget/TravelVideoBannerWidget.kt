package com.tokopedia.common.travel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.databinding.WidgetTravelVideoBannerBinding
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by furqan on 02/11/2020
 */
class TravelVideoBannerWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
    private lateinit var bannerModel: TravelVideoBannerModel

    private val binding = WidgetTravelVideoBannerBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(collectiveBannerModel: TravelCollectiveBannerModel) {
        bannerModel = TravelVideoBannerModel()
        collectiveBannerModel.let {
            if (it.banners.isNotEmpty()) {
                bannerModel.title = it.meta.label
                bannerModel.id = it.banners[0].id
                bannerModel.imageUrl = it.banners[0].attribute.imageUrl
                bannerModel.description = it.banners[0].attribute.description
                bannerModel.destinationLink =
                    if (it.banners[0].attribute.appUrl.isNotEmpty()) {
                        it.banners[0].attribute.appUrl
                    } else {
                        it.banners[0].attribute.webUrl
                    }
            }
        }
    }

    fun setData(bannerModel: TravelVideoBannerModel) {
        this.bannerModel = bannerModel
    }

    fun getData(): TravelVideoBannerModel = bannerModel

    fun build() {
        with(binding) {
            if (::bannerModel.isInitialized) {
                if (bannerModel.title.isNotEmpty()) {
                    tgTravelVideoBannerTitle.text = bannerModel.title
                    tgTravelVideoBannerTitle.visibility = View.VISIBLE
                } else {
                    tgTravelVideoBannerTitle.visibility = View.GONE
                }

                if (bannerModel.imageUrl.isNotEmpty() &&
                    bannerModel.destinationLink.isNotEmpty()
                ) {
                    ivTravelVideoBanner.loadImage(bannerModel.imageUrl)
                    ivTravelVideoBanner.setOnClickListener {
                        listener?.onVideoBannerClicked(bannerModel)
                        RouteManager.route(context, bannerModel.destinationLink)
                    }
                    showTravelVideoBanner()
                } else {
                    hideTravelVideoBanner()
                }
            } else {
                hideTravelVideoBanner()
            }
        }
    }

    fun showTravelVideoBanner() {
        binding.containerTravelVideoBanner.visibility = View.VISIBLE
    }

    fun hideTravelVideoBanner() {
        binding.containerTravelVideoBanner.visibility = View.GONE
    }

    interface ActionListener {
        fun onVideoBannerClicked(bannerData: TravelVideoBannerModel)
    }
}
