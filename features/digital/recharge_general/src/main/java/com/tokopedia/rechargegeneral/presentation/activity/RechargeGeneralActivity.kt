package com.tokopedia.rechargegeneral.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.rechargegeneral.di.DaggerRechargeGeneralComponent
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment


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

    companion object {

        val PARAM_CATEGORY_ID = "category_id"
        val PARAM_MENU_ID = "menu_id"
        val PARAM_OPERATOR_ID = "operator_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"

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
}