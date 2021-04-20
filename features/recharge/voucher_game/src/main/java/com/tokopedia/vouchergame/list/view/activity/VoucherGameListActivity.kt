package com.tokopedia.vouchergame.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.list.di.DaggerVoucherGameListComponent
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.fragment.VoucherGameListFragment
import timber.log.Timber

/**
 * Created by resakemal on 12/08/19.
 *
 * applink
 * tokopedia://digital/form?category_id=6&menu_id=4&template=voucher
 * or
 * @sample voucherGame = RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, "6", "4", "voucher")
 */

class VoucherGameListActivity : BaseVoucherGameActivity(), HasComponent<VoucherGameListComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val voucherGameExtraParam = VoucherGameExtraParam(
                bundle?.getString(PARAM_CATEGORY_ID) ?: "",
                bundle?.getString(PARAM_PRODUCT_ID) ?: "",
                bundle?.getString(PARAM_MENU_ID) ?: "",
                bundle?.getString(PARAM_OPERATOR_ID) ?: ""
        )
        val rechargeProductFromSlice = bundle?.getString(RECHARGE_PRODUCT_EXTRA) ?: ""
        return VoucherGameListFragment.newInstance(voucherGameExtraParam, rechargeProductFromSlice)
    }

    override fun getComponent(): VoucherGameListComponent {
        return DaggerVoucherGameListComponent.builder()
                .voucherGameComponent(getVoucherGameComponent())
                .build()
    }

    override fun getLayoutRes(): Int {
        return R.layout.vg_activity
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun shouldShowOptionMenu(): Boolean {
        return true
    }


    companion object {

        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"

        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"


        fun newInstance(context: Context, categoryId: String, menuId: String, operatorId: String = "", productId: String = ""): Intent {
            val intent = Intent(context, VoucherGameListActivity::class.java)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            intent.putExtra(PARAM_MENU_ID, menuId)
            intent.putExtra(PARAM_OPERATOR_ID, operatorId)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            return intent
        }
    }

    override fun onBackPressed() {
        (fragment as VoucherGameListFragment).onBackPressed()
        super.onBackPressed()
    }
}