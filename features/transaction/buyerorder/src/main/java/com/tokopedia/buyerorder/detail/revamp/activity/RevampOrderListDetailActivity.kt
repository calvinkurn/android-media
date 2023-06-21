package com.tokopedia.buyerorder.detail.revamp.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.revamp.fragment.OmsDetailFragment as NewOMSFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class RevampOrderListDetailActivity: BaseSimpleActivity(), HasComponent<OrderDetailsComponent> {

    private var category: String? = null
    private var upstream: String? = null
    private var orderId: String? = null
    private var fromPayment: String = "false"

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getNewFragment(): Fragment {
        return getSwitchedFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        intent.data?.let {
            category = it.toString()
            fromPayment = it.getQueryParameter(FROM_PAYMENT) ?: ""

            intent.extras?.let { _ ->
                orderId = intent.getStringExtra(ORDER_ID)
            }
        }
        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        } else {
            intent.data?.let {
                upstream = it.getQueryParameter(UPSTREAM)
            }
        }
        super.onCreate(savedInstanceState)

        if (fromPayment.equals("true", true)){
            updateTitle(getString(com.tokopedia.buyerorder.R.string.thank_you))
        }

    }

    private fun getSwitchedFragment(): Fragment {
        return NewOMSFragment.getInstance(
                orderId ?: "",
                "",
                fromPayment,
                upstream?:""
            )
    }

    override fun getComponent(): OrderDetailsComponent =
        DaggerOrderDetailsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            intent.data?.let {
                category = it.toString()
                upstream = it.getQueryParameter(UPSTREAM)
            }

            category?.let {
                val formattedCategory = it.uppercase()

                if (formattedCategory.isNotEmpty()){
                    supportFragmentManager.beginTransaction()
                        .add(com.tokopedia.abstraction.R.id.parent_view, getSwitchedFragment())
                        .commit()
                }
            }
        } else {
            finish()
        }
    }

    companion object{

        private const val ORDER_ID = "order_id"
        private const val REQUEST_CODE_LOGIN = 100
        private const val UPSTREAM = "upstream"
        private const val FROM_PAYMENT = "from_payment"

        fun getIntent(context: Context, orderId: String, data: Uri?): Intent{
            return Intent(context, RevampOrderListDetailActivity::class.java).apply {
                putExtra(ORDER_ID, orderId)
                setData(data)
            }
        }
    }
}
