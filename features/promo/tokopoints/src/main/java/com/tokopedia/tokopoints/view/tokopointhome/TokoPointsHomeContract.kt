package com.tokopedia.tokopoints.view.tokopointhome

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.section.SectionContent

interface TokoPointsHomeContract {
    interface View : CustomerView {
        fun onError(error: Int, hasInternet: Boolean)
        val appContext: Context
        val activityContext: Context

        fun openWebView(url: String)
        fun hideLoading()
        fun showLoading()
        fun renderRewardUi(
            topSectionResponse: TopSectionResponse?,
            sections: List<SectionContent>,
            rewardsRecommendation: RewardsRecommendation?
        )
    }

    interface Presenter {
        fun getTokoPointDetail()
    }
}
