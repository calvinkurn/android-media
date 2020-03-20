package com.tokopedia.thankyou_native.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.thankyou_native.di.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.InstantPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.LoaderFragment
import com.tokopedia.thankyou_native.presentation.fragment.ProcessingPaymentFragment


class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent>, ThankYouPageDataLoadCallback {

    //todo handle toolbar back-press button and toolbar icon

    //processing 720644
    //instant credit card 720599

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.putExtra(ARG_MERCHANT, "tokopediatest")
        intent.putExtra(ARG_PAYMENT_ID, 720598L)
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return LoaderFragment.getLoaderFragmentInstance(bundle)
    }

    override fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData) {
        val fragment = getGetFragmentByPaymentMode(thanksPageData)
        fragment?.let {
            supportFragmentManager.beginTransaction()
                    .replace(parentViewResourceID, fragment, tagFragment)
                    .commit()
        } ?: kotlin.run {
            finish()
        }
    }

    override fun getComponent(): ThankYouPageComponent = DaggerThankYouPageComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication)
                    .baseAppComponent).build()

    private fun getGetFragmentByPaymentMode(thanksPageData: ThanksPageData): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return when (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)) {
            is ProcessingPaymentPage -> {
                title = ProcessingPaymentFragment.SCREEN_NAME
                ProcessingPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            is InstantPaymentPage -> {
                title = InstantPaymentFragment.SCREEN_NAME
                InstantPaymentFragment.getLoaderFragmentInstance(bundle, thanksPageData)
            }
            is WaitingPaymentPage -> {
                title = DeferredPaymentFragment.SCREEN_NAME
                DeferredPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            else -> null
        }

    }

    /**
     * this function is override because need to check payment
     * status if payment type is deferred/Processing
     * */
    override fun onBackPressed() {
        if (!isOnBackPressOverride())
            super.onBackPressed()
    }

    private fun isOnBackPressOverride(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            if (it is DeferredPaymentFragment) {
                return it.onBackPressed()
            } else if (it is ProcessingPaymentFragment) {
                return it.onBackPressed()
            }

        }
        return false
    }

    companion object {

        const val ARG_PAYMENT_ID = "paymentID"
        const val ARG_MERCHANT = "merchant"

        fun createIntent(context: Context, paymentID: String, merchant: String) = Intent(context, ThankYouPageActivity::class.java).apply {
            putExtra(ARG_MERCHANT, merchant)
            putExtra(ARG_PAYMENT_ID, paymentID)
        }
    }
}

interface ThankYouPageDataLoadCallback {
    fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData)
}