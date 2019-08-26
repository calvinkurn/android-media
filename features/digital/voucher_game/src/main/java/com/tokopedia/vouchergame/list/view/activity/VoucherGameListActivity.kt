package com.tokopedia.vouchergame.list.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.list.di.DaggerVoucherGameListComponent
import com.tokopedia.vouchergame.list.di.VoucherGameListComponent
import com.tokopedia.vouchergame.list.view.fragment.VoucherGameListFragment

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListActivity : BaseSimpleActivity(), HasComponent<VoucherGameListComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val voucherGameExtraParam = VoucherGameExtraParam(
                bundle?.getString(PARAM_CATEGORY_ID) ?: "",
                bundle?.getString(PARAM_PRODUCT_ID) ?: "",
                bundle?.getString(PARAM_MENU_ID) ?: "",
                bundle?.getString(PARAM_OPERATOR_ID) ?: ""
        )
        return VoucherGameListFragment.newInstance(voucherGameExtraParam)
    }

    override fun getComponent(): VoucherGameListComponent {
        return DaggerVoucherGameListComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {

        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"

        fun newInstance(context: Context, categoryId: String, menuId: String, operatorId: String = "", productId: String = ""): Intent {
            val intent = Intent(context, VoucherGameListActivity::class.java)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            intent.putExtra(PARAM_MENU_ID, menuId)
            intent.putExtra(PARAM_OPERATOR_ID, operatorId)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            return intent
        }
    }
}