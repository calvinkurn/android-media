package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.MonthlyNewBuyer
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.viewModel.MonthlyBuyerViewModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


/**
 *
 * this is headless Fragment with no UI
 * this fragment is loading Monthly buyer independent of user interaction
 * also sending event for thank page data loaded to GTM(transaction), Appsflyer(purchase complete), Branch
 *
 * */
class ThanksPageAnalyticsFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var thankYouPageAnalytics: ThankYouPageAnalytics

    lateinit var monthlyBuyerViewModel: MonthlyBuyerViewModel

    lateinit var thanksPageData: ThanksPageData


    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initViewModel()
        startObservingViewModel()
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        monthlyBuyerViewModel = viewModelProvider.get(MonthlyBuyerViewModel::class.java)
    }

    private fun postThanksPageLoadEvent(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        thankYouPageAnalytics.sendThankYouPageDataLoadEvent(thanksPageData)
        thankYouPageAnalytics.appsFlyerPurchaseEvent(thanksPageData)
        monthlyBuyerViewModel.getMonthlyBuyerStatus(thanksPageData.shopOrder.first().orderId)
    }

    private fun startObservingViewModel() {
        monthlyBuyerViewModel.monthlyNewBuyerResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> postBranchPurchaseEvent(it.data)
            }
        })
    }

    private fun postBranchPurchaseEvent(data: MonthlyNewBuyer) {
        thankYouPageAnalytics.sendBranchIOEvent(thanksPageData, data)
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