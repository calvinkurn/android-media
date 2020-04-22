package com.tokopedia.thankyou_native.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.fragment.*
import kotlinx.android.synthetic.main.thank_activity_thank_you.*


class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent>,
        ThankYouPageDataLoadCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("")
    }

    override fun getLayoutRes() = R.layout.thank_activity_thank_you

    override fun getToolbarResourceID() = R.id.thank_header

    override fun getParentViewResourceID(): Int = R.id.thank_parent_view


    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        //todo remove magical string...
        intent.data?.getQueryParameter("payment_id")?.let {
            intent.putExtra(ARG_MERCHANT, intent.data?.getQueryParameter("merchant"))
            intent.putExtra(ARG_PAYMENT_ID, it.toLong())
            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
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

    fun getHeader(): HeaderUnify = thank_header

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
                updateHeaderTitle(ProcessingPaymentFragment.SCREEN_NAME)
                ProcessingPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            is InstantPaymentPage -> {
                updateHeaderTitle(InstantPaymentFragment.SCREEN_NAME)
                InstantPaymentFragment.getFragmentInstance(bundle, thanksPageData)
            }
            is WaitingPaymentPage -> {
                val paymentType = PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)
                return if (paymentType == CashOnDelivery) {
                    updateHeaderTitle(CashOnDeliveryFragment.SCREEN_NAME)
                    CashOnDeliveryFragment.getFragmentInstance(bundle, thanksPageData)
                } else {
                    updateHeaderTitle(DeferredPaymentFragment.SCREEN_NAME)
                    DeferredPaymentFragment.getFragmentInstance(bundle, thanksPageData)
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
