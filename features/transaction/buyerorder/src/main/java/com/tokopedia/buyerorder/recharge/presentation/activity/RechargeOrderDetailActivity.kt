package com.tokopedia.buyerorder.recharge.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.OrderCategory
import com.tokopedia.buyerorder.detail.revamp.activity.RevampOrderListDetailActivity
import com.tokopedia.buyerorder.recharge.di.DaggerRechargeOrderDetailComponent
import com.tokopedia.buyerorder.recharge.di.RechargeOrderDetailComponent
import com.tokopedia.buyerorder.recharge.presentation.fragment.RechargeOrderDetailFragment
import com.tokopedia.user.session.UserSession

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailActivity : BaseSimpleActivity(), HasComponent<RechargeOrderDetailComponent> {

    private var fromPayment: Boolean = false
    private lateinit var orderId: String
    private var category: String? = null
    private var upstream: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.let { mIntent ->
            mIntent.data?.let { mData ->
                category = mData.toString()

                mIntent.extras?.let { mExtras ->
                    orderId = mIntent.getStringExtra(ORDER_ID) ?: ""
                }

                fromPayment = try {
                    mData.getQueryParameter(FROM_PAYMENT).toBoolean()
                } catch (t: Throwable) {
                    false
                }
                upstream = mData.getQueryParameter(UPSTREAM)
            }
        }

        if (!UserSession(this).isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE)
        }

        super.onCreate(savedInstanceState)

        if (fromPayment) {
            updateTitle(resources.getString(R.string.thank_you))
        }

    }

    override fun getNewFragment(): Fragment? {
        category?.let {
            category = it.uppercase()

            return if (it.contains(OrderCategory.DIGITAL, true)) {
                RechargeOrderDetailFragment.getInstance(orderId, OrderCategory.DIGITAL)
            } else {
                startActivity(RevampOrderListDetailActivity.getIntent(this, orderId, intent.data))
                finish()
                null
            }
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                intent?.let { mIntent ->
                    mIntent.data?.let { mData ->
                        category = mData.toString()
                        upstream = mData.getQueryParameter(UPSTREAM)
                    }
                }

                category?.let {
                    category = it.uppercase()

                    if (it.contains(OrderCategory.DIGITAL, true)) {
                        supportFragmentManager.beginTransaction()
                                .add(com.tokopedia.abstraction.R.id.parent_view, RechargeOrderDetailFragment.getInstance(orderId, OrderCategory.DIGITAL))
                                .commit()
                    } else {
                        startActivity(RevampOrderListDetailActivity.getIntent(this, orderId, intent.data))
                        finish()
                    }
                }
            } else {
                finish()
            }
        }
    }

    override fun getComponent(): RechargeOrderDetailComponent =
            DaggerRechargeOrderDetailComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    companion object {
        const val ORDER_ID = "order_id"
        const val FROM_PAYMENT = "from_payment"
        const val UPSTREAM = "upstream"
        const val REQUEST_CODE = 100
    }

}
