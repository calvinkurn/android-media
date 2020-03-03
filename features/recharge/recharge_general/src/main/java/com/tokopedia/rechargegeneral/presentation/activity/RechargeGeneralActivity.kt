package com.tokopedia.rechargegeneral.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.rechargegeneral.di.DaggerRechargeGeneralComponent
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import timber.log.Timber

/**
 * applink
 * tokopedia://digital/form?category_id=5&menu_id=120&template=general
 * or
 * @sample airPDAM = RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, "5", "120", "general")
 */

class RechargeGeneralActivity : BaseSimpleActivity(), HasComponent<RechargeGeneralComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID)?.toIntOrNull() ?: 0
        val menuId = bundle?.getString(PARAM_MENU_ID)?.toIntOrNull() ?: 0
        val operatorId = bundle?.getString(PARAM_OPERATOR_ID)?.toIntOrNull() ?: 0
        val productId = bundle?.getString(PARAM_PRODUCT_ID) ?: ""
        return RechargeGeneralFragment.newInstance(categoryId, menuId, operatorId, productId)
    }

    override fun getComponent(): RechargeGeneralComponent {
        return DaggerRechargeGeneralComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as RechargeGeneralFragment).onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.handleExtra()
    }

    companion object {

        val PARAM_CATEGORY_ID = "category_id"
        val PARAM_MENU_ID = "menu_id"
        val PARAM_OPERATOR_ID = "operator_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"

        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"

        fun newInstance(context: Context,
                        categoryId: Int,
                        menuId: Int,
                        operatorId: Int = 0,
                        productId: String = ""): Intent {
            val intent = Intent(context, RechargeGeneralActivity::class.java)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId.toString())
            intent.putExtra(PARAM_MENU_ID, menuId.toString())
            intent.putExtra(PARAM_OPERATOR_ID, operatorId.toString())
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            return intent
        }
    }

    /* This Method is use to tracking action click when user click and redirect to RechargeGeneral
    */
    private fun Intent.handleExtra() {
        if (intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            Timber.d("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
        }
    }
}
