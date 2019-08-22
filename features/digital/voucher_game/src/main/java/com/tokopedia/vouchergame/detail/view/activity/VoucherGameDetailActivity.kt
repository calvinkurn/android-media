package com.tokopedia.vouchergame.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.vouchergame.detail.di.DaggerVoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailActivity : BaseSimpleActivity(), HasComponent<VoucherGameDetailComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val menuId = bundle?.getInt(VoucherGameDetailFragment.EXTRA_MENU_ID) ?: 0
        val platformId = bundle?.getInt(VoucherGameDetailFragment.EXTRA_PLATFORM_ID) ?: 0
        val operator = bundle?.getString(VoucherGameDetailFragment.EXTRA_OPERATOR_ID) ?: ""
        return VoucherGameDetailFragment.createInstance(menuId, platformId, operator)
    }

    override fun getComponent(): VoucherGameDetailComponent {
        return DaggerVoucherGameDetailComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    companion object {
        fun newInstance(context: Context, menuId: Int, platformId: Int, operator: String): Intent {
            val intent = Intent(context, VoucherGameDetailActivity::class.java)
            intent.putExtra(VoucherGameDetailFragment.EXTRA_MENU_ID, menuId)
            intent.putExtra(VoucherGameDetailFragment.EXTRA_PLATFORM_ID, platformId)
            intent.putExtra(VoucherGameDetailFragment.EXTRA_OPERATOR_ID, operator)
            return intent
        }
    }
}