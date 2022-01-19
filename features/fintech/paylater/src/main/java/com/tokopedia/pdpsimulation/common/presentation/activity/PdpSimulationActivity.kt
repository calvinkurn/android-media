package com.tokopedia.pdpsimulation.common.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.di.component.DaggerPdpSimulationComponent
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.presentation.fragment.PdpSimulationFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class PdpSimulationActivity : BaseSimpleActivity(), HasComponent<PdpSimulationComponent> {

    private val pdpSimulationComponent: PdpSimulationComponent by lazy { initInjector() }
    private val REQUEST_CODE_LOGIN = 123

    @Inject
    lateinit var userSession: UserSessionInterface


    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        pdpSimulationComponent.inject(this)
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.pdp_simulation_header_title))
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
                //bundle.putString(PRODUCT_PRICE, it.getString(PRODUCT_PRICE))
                //bundle.putString(PARAM_PRODUCT_URL, it.getString(PARAM_PRODUCT_URL))
                bundle.putString(PARAM_PRODUCT_ID, it.getString(PARAM_PRODUCT_ID))
            }
            PdpSimulationFragment.newInstance(bundle)
        }
    }


    companion object {
        const val SCREEN_NAME = "PayLater & Cicilan"
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
}