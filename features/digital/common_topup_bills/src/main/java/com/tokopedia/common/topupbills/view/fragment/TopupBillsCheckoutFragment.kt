package com.tokopedia.common.topupbills.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.DaggerCommonTopupBillsComponent
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class TopupBillsCheckoutFragment: BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var checkoutPassData: DigitalCheckoutPassData

    private lateinit var totalPrice: TextView
    private lateinit var nextButton: Button
    private lateinit var buyLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_digital_buy_telco, container, false)
        totalPrice = view.findViewById(R.id.total_price)
        nextButton = view.findViewById(R.id.next_btn)
        buyLayout = view.findViewById(R.id.buy_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initView() {
        nextButton.setOnClickListener {
            processToCart()
        }
    }

    fun setTotalPrice(price: String) {
        totalPrice.setText(price)
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            buyLayout.visibility = View.VISIBLE
        } else {
            buyLayout.visibility = View.GONE
        }
    }

    private fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }

    private fun navigateToCart() {
        if (::checkoutPassData.isInitialized) {
            val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        activity?.let {
            val commonTopupBillsComponent: CommonTopupBillsComponent by lazy {
                DaggerCommonTopupBillsComponent.builder()
                        .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                        .build()
            }
            commonTopupBillsComponent.inject(this)
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
    }
}