package com.tokopedia.pdpsimulation.activateCheckout.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.listner.GatewaySelectActivityListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment.ActivationCheckoutFragment
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.constants.PARAM_CATEGORY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_CODE
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PARENT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.di.component.DaggerPdpSimulationComponent
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class OptimizedCheckoutActivity : BaseSimpleActivity(), HasComponent<PdpSimulationComponent>,
    GatewaySelectActivityListner, PdpSimulationCallback {

    private val pdpSimulationComponent: PdpSimulationComponent by lazy { initInjector() }
    var productId:String? = null

    @Inject
    lateinit var pdpSimulationAnalytics: dagger.Lazy<PdpSimulationAnalytics>

    @Inject
    lateinit var userSession: UserSessionInterface


    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        pdpSimulationComponent.inject(this)
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.pdp_activation_header_title))
    }

    override fun getLayoutRes() = R.layout.activity_pdp_simulation

    override fun getToolbarResourceID() = R.id.pdpSimulationHeader

    override fun getParentViewResourceID(): Int = R.id.pdpSimulationParentView

    override fun getNewFragment(): Fragment? {

        return if (!userSession.isLoggedIn) {
            startActivityForResult(
                RouteManager.getIntent(this, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
            null
        } else {
            val bundle = Bundle()
            intent.extras?.let {
                productId = it.getString(PARAM_PRODUCT_ID)
                bundle.putString(PARAM_GATEWAY_ID, it.getString(PARAM_GATEWAY_ID))
                bundle.putString(PARAM_PRODUCT_ID, it.getString(PARAM_PRODUCT_ID))
                bundle.putString(PARAM_PRODUCT_TENURE, it.getString(PARAM_PRODUCT_TENURE))
                bundle.putString(PARAM_GATEWAY_CODE, it.getString(PARAM_GATEWAY_CODE))
                bundle.putString(PARAM_PARENT_ID, it.getString(PARAM_PARENT_ID))
                bundle.putString(PARAM_CATEGORY_ID, it.getString(PARAM_CATEGORY_ID))
            }

            return ActivationCheckoutFragment.newInstance(bundle)
        }
    }


    companion object {
        const val SCREEN_NAME = "Beli Pakai PayLater"
        const val REQUEST_CODE_LOGIN = 123
    }

    private fun initInjector() =
        DaggerPdpSimulationComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()


    /**
     * This method is to restart the activity
     */
    private fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn)
                    restartActivity()
                else
                    finish()
            }
        }
    }

    override fun getComponent() = pdpSimulationComponent
    override fun setGatewayValue(gatewaySelected: String) {
        (this.fragment as ActivationCheckoutFragment).updateSelectedTenure(gatewaySelected)
    }

    override fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>) {
    }

    override fun sendOtherAnalytics(pdpSimulationEvent: PdpSimulationEvent) {
        pdpSimulationAnalytics.get().sendOccEvent(pdpSimulationEvent)
    }

    override fun sendAnalytics(pdpSimulationEvent: PayLaterAnalyticsBase) {
        pdpSimulationEvent.productId = productId?:""
        pdpSimulationAnalytics.get().sendPayLaterSimulationEvent(pdpSimulationEvent)
    }


}
