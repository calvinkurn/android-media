package com.tokopedia.thankyou_native.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.nps.helper.InAppReviewHelper
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackRatingBottomSheet
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.fragment.*
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import kotlinx.android.synthetic.main.thank_activity_thank_you.*
import javax.inject.Inject

class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent>,
        ThankYouPageDataLoadCallback {

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>

    private lateinit var thankYouPageComponent: ThankYouPageComponent

    lateinit var thanksPageData: ThanksPageData

    fun getHeader(): HeaderUnify = thank_header

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("")
        component.inject(this)
    }

    override fun getLayoutRes() = R.layout.thank_activity_thank_you

    override fun getToolbarResourceID() = R.id.thank_header

    override fun getParentViewResourceID(): Int = R.id.thank_parent_view

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.data?.getQueryParameter(ARG_PAYMENT_ID)?.let {
            intent.putExtra(ARG_MERCHANT, intent.data?.getQueryParameter(ARG_MERCHANT))
            intent.putExtra(ARG_PAYMENT_ID, it.toLong())
            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
        }
        thank_header.isShowBackButton = false
        return LoaderFragment.getLoaderFragmentInstance(bundle)
    }

    override fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData) {
        this.thanksPageData = thanksPageData
        val fragment = getGetFragmentByPaymentMode(thanksPageData)
        fragment?.let {
            supportFragmentManager.beginTransaction()
                    .replace(parentViewResourceID, fragment, tagFragment)
                    .commit()
        } ?: run { gotoHomePage() }
        showAppFeedbackBottomSheet(thanksPageData)
        postEventOnThankPageDataLoaded(thanksPageData)
    }

    private fun showAppFeedbackBottomSheet(thanksPageData: ThanksPageData){
        val paymentStatus = PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)
        if(paymentStatus == PaymentVerified || paymentStatus == PaymentPreAuth) {
            if (!InAppReviewHelper.launchInAppReview(this, null)) {
                val rating = AppFeedbackRatingBottomSheet()
                rating.showDialog(supportFragmentManager, this)
            }
        }
    }

    private fun postEventOnThankPageDataLoaded(thanksPageData: ThanksPageData) {
        thankYouPageAnalytics.get().postThankYouPageLoadedEvent(thanksPageData)
    }

    override fun onInvalidThankYouPage() {
        gotoHomePage()
        finish()
    }

    override fun getComponent(): ThankYouPageComponent {
        if (!::thankYouPageComponent.isInitialized)
            thankYouPageComponent = DaggerThankYouPageComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication)
                            .baseAppComponent).build()
        return thankYouPageComponent
    }

    private fun getGetFragmentByPaymentMode(thanksPageData: ThanksPageData): Fragment? {
        thank_header.isShowBackButton = true
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return when (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)) {
            is ProcessingPaymentPage -> {
                updateHeaderTitle(ProcessingPaymentFragment.SCREEN_NAME)
                ProcessingPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            is InstantPaymentPage -> {
                updateHeaderTitle(InstantPaymentFragment.SCREEN_NAME)
                InstantPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            is WaitingPaymentPage -> {
                return when (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)) {
                    is PaymentWaitingCOD -> {
                        updateHeaderTitle(CashOnDeliveryFragment.SCREEN_NAME)
                        CashOnDeliveryFragment.getFragmentInstance(bundle, thanksPageData)
                    }
                    is PaymentWaiting -> {
                        updateHeaderTitle(DeferredPaymentFragment.SCREEN_NAME)
                        DeferredPaymentFragment.getFragmentInstance(bundle, thanksPageData)
                    }
                    else -> null
                }
            }
            else -> null
        }

    }

    private fun updateHeaderTitle(screenName: String) {
        thank_header.title = screenName
    }

    /**
     * this function is override because need to check payment
     * status if payment type is deferred/Processing
     * */
    override fun onBackPressed() {
        if(::thanksPageData.isInitialized)
            thankYouPageAnalytics.get().sendBackPressedEvent(thanksPageData.paymentID.toString())
        if (!isOnBackPressOverride()) {
            gotoHomePage()
            finish()
        }
    }

    private fun isOnBackPressOverride(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            return when (it) {
                is LoaderFragment -> true
                else -> false
            }

        }
        return false
    }

    private fun gotoHomePage() {
        RouteManager.route(this, ApplinkConst.HOME, "")
        finish()
    }

    companion object {
        const val SCREEN_NAME = "Finish Transaction"
        const val ARG_PAYMENT_ID = "payment_id"
        const val ARG_MERCHANT = "merchant"
    }
}

