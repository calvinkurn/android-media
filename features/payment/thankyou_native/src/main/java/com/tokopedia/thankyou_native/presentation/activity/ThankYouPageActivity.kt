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
import com.tokopedia.thankyou_native.domain.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.InstantPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.LoaderFragment


class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent>, ThankYouPageDataLoadCallback {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
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
        return when (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus)) {
            is PaymentVerified -> {
                InstantPaymentFragment.getLoaderFragmentInstance(Bundle(), thanksPageData)
            }
            is PaymentWaiting -> {
                DeferredPaymentFragment.getFragmentInstance(thanksPageData)
            }
            is PaymentExpired -> {

            }
            is PaymentActive -> {

            }
            is PaymentCancelled -> {
            }
            is PaymentVoid -> {
            }
            is PaymentPreAuth -> {
            }
            is PaymentWaitingCOD -> {
            }
            else -> null
        }

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