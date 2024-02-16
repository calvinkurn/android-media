package com.tokopedia.thankyou_native.presentation.views.listener

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

interface ThankYouBaseInterface {
    fun getRecommendationContainer(): LinearLayout?
    fun getFeatureListingContainer(): GyroView?
    fun getTopAdsView(): TopAdsView?
    fun bindThanksPageDataToUI(thanksPageData: ThanksPageData)
    fun getLoadingView(): View?
    fun onThankYouPageDataReLoaded(data: ThanksPageData)
    fun getTopTickerView(): Ticker?
    fun getBottomContentRecyclerView(): RecyclerView?
    fun getBannerTitle(): Typography?
    fun getBannerCarousel(): CarouselUnify?
}
