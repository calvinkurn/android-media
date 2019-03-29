package com.tokopedia.cod.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.cod.di.DaggerCodComponent
import com.tokopedia.logisticanalytics.CodAnalytics
import com.tokopedia.transactiondata.constant.Constant
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import com.tokopedia.transactiondata.entity.response.cod.Data
import javax.inject.Inject

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodActivity : BaseSimpleActivity() {

    companion object {

        const val EXTRA_COD_DATA = "EXTRA_COD_DATA"

        @JvmStatic
        fun newIntent(context: Context, data: Data): Intent =
                Intent(context, CodActivity::class.java).apply {
                    putExtra(EXTRA_COD_DATA, data)
                }
    }

    @Inject lateinit var mTracker: CodAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.let {
            if (it is BaseMainApplication) {
                DaggerCodComponent.builder()
                        .baseAppComponent(it.baseAppComponent)
                        .build().inject(this)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        val data: Data = intent.getParcelableExtra(EXTRA_COD_DATA)
        val checkoutRequest: CheckoutRequest = intent.getParcelableExtra(Constant.EXTRA_CHECKOUT_REQUEST)
        return CodFragment.newInstance(data, checkoutRequest)
    }

    override fun onBackPressed() {
        mTracker.eventClickBackOnConfirmation()
        super.onBackPressed()
    }

}