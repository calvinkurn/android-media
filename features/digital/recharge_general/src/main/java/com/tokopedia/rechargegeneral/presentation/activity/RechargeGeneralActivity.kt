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
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        val menuId = bundle?.getInt(PARAM_MENU_ID) ?: 0
        return RechargeGeneralFragment.newInstance(categoryId, menuId)
    }

    override fun getComponent(): RechargeGeneralComponent {
        return DaggerRechargeGeneralComponent.builder()
                    .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                    .build()
    }

    companion object {

        val PARAM_CATEGORY_ID = "category_id"
        val PARAM_MENU_ID = "menu_id"

        fun newInstance(context: Context, categoryId: String, menuId: Int): Intent {
            val intent = Intent(context, RechargeGeneralActivity::class.java)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            intent.putExtra(PARAM_MENU_ID, menuId)
            return intent
        }
    }
}