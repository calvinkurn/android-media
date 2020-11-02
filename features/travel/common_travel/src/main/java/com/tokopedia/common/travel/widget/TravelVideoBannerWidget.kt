package com.tokopedia.common.travel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.viewmodel.TravelVideoBannerViewModel
import com.tokopedia.unifycomponents.BaseCustomView
import javax.inject.Inject

/**
 * @author by furqan on 02/11/2020
 */
class TravelVideoBannerWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    @Inject

    private lateinit var travelVideoBannerViewModel: TravelVideoBannerViewModel
    private lateinit var videoBannerType: TravelType

    init {
        View.inflate(context, R.layout.widget_travel_video_banner, this)
    }



    fun setTravelType(travelType: TravelType) {
        videoBannerType = travelType
    }

}