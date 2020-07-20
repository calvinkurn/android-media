package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.MarketPlaceThankPage
import com.tokopedia.thankyou_native.data.mapper.ThankPageTypeMapper
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import javax.inject.Inject


/**
 *
 * this is headless Fragment with no UI
 * also sending event for thank page data loaded to GTM(transaction), Appsflyer(purchase complete), Branch
 *
 * */
class ThanksPageAnalyticsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>


    lateinit var thanksPageData: ThanksPageData


    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private fun postThanksPageLoadEvent(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        if (thanksPageData.pushGtm) {
            when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
                MarketPlaceThankPage -> thankYouPageAnalytics.get().sendThankYouPageDataLoadEvent(thanksPageData)
                else -> thankYouPageAnalytics.get().sendigitalThankYouPageDataLoadEvent(thanksPageData)
            }
        } else {
            thankYouPageAnalytics.get().sendPushGtmFalseEvent(thanksPageData.paymentID.toString())
        }
        thankYouPageAnalytics.get().appsFlyerPurchaseEvent(thanksPageData)
        thankYouPageAnalytics.get().sendBranchIOEvent(thanksPageData)

    }

    companion object {
        private const val TAG = "ThanksPageAnalyticsFragment"

        fun addFragmentToActivity(fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                    .add(ThanksPageAnalyticsFragment(),
                            TAG).commit();
        }

        fun postThanksPageLoadEvent(fragmentManager: FragmentManager,
                                    thanksPageData: ThanksPageData) {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            fragment?.let {
                if (fragment is ThanksPageAnalyticsFragment) {
                    fragment.postThanksPageLoadEvent(thanksPageData)
                }
            }
        }
    }

}