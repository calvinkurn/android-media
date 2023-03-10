package com.tokopedia.recharge_credit_card

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.recharge_credit_card.analytics.CreditCardAnalytics
import com.tokopedia.recharge_credit_card.di.DaggerRechargeCCComponent
import com.tokopedia.recharge_credit_card.di.RechargeCCComponent
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/*
 * applink production = tokopedia://digital/form?category_id=26&menu_id=169&template=tagihancc
 * sample instant checkout applink = tokopedia://digital/form?operator_id=18&category_id=26&product_id=269&signature=asdasassa&identifier=asdaszz&client_number=04898****398
 * applink staging = tokopedia://digital/form?category_id=26&menu_id=86&template=tagihancc
 * for activating staging, dont forget change base url on submit PCIDSS
 */

class RechargeCCActivity : BaseSimpleActivity(), HasComponent<RechargeCCComponent> {

    @Inject
    lateinit var creditCardAnalytics: CreditCardAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var toolbarCreditCard: HeaderUnify? = null

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID, CATEGORY_ID_DEFAULT)
            ?: CATEGORY_ID_DEFAULT
        val menuId = bundle?.getString(PARAM_MENU_ID, MENU_ID_DEFAULT) ?: MENU_ID_DEFAULT

        val operatorId = bundle?.getString(PARAM_OPERATOR_ID, "") ?: ""
        val productId = bundle?.getString(PARAM_PRODUCT_ID, "") ?: ""
        val signature = bundle?.getString(PARAM_SIGNATURE, "") ?: ""
        val identifier = bundle?.getString(PARAM_IDENTIFIER, "") ?: ""
        val clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER, "") ?: ""
        return RechargeCCFragment.newInstance(categoryId, menuId, operatorId, productId, signature, identifier, clientNumber)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_recharge_cc
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_credit_card
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarCreditCard = findViewById(R.id.toolbar_credit_card)

        setSecureWindowFlag()
        component.inject(this)

        toolbarCreditCard?.addRightIcon(com.tokopedia.common_digital.R.drawable.digital_common_ic_tagihan)
        toolbarCreditCard?.rightIcons?.let {
            it[0].setOnClickListener {
                RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sendAnalyticsOpenScreen()
    }

    private fun sendAnalyticsOpenScreen() {
        creditCardAnalytics.openCCScreen()
    }

    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) }
        }
    }

    override fun getComponent(): RechargeCCComponent {
        return DaggerRechargeCCComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        private const val PARAM_MENU_ID = "menu_id"
        private const val PARAM_CATEGORY_ID = "category_id"

        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_SIGNATURE = "signature"
        const val PARAM_IDENTIFIER = "identifier"
        const val PARAM_CLIENT_NUMBER = "client_number"

        private const val CATEGORY_ID_DEFAULT = "26"
        private const val MENU_ID_DEFAULT = "169"
    }
}
