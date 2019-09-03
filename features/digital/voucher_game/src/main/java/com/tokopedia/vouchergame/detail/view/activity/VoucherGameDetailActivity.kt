package com.tokopedia.vouchergame.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common_digital.common.constant.DigitalExtraParam.EXTRA_PARAM_TELCO
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.di.DaggerVoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.di.VoucherGameDetailComponent
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment.Companion.EXTRA_PARAM_OPERATOR_DATA
import com.tokopedia.vouchergame.list.view.model.VoucherGameOperatorAttributes

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailActivity : BaseVoucherGameActivity(), HasComponent<VoucherGameDetailComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val voucherGameExtraParam: VoucherGameExtraParam =
                bundle?.getParcelable(EXTRA_PARAM_TELCO) ?: VoucherGameExtraParam()
        val voucherGameOperatorAttributes: VoucherGameOperatorAttributes =
                bundle?.getParcelable(EXTRA_PARAM_OPERATOR_DATA) ?: VoucherGameOperatorAttributes()
        return VoucherGameDetailFragment.newInstance(voucherGameExtraParam, voucherGameOperatorAttributes)
    }

    override fun getComponent(): VoucherGameDetailComponent {
        return DaggerVoucherGameDetailComponent.builder()
                .voucherGameComponent(getVoucherGameComponent())
                .build()
    }

    override fun shouldShowOptionMenu(): Boolean { return true }

    companion object {

        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"

        fun newInstance(context: Context,
                        voucherGameExtraParam: VoucherGameExtraParam,
                        voucherGameOperatorAttributes: VoucherGameOperatorAttributes): Intent {
            val intent = Intent(context, VoucherGameDetailActivity::class.java)
            intent.putExtra(EXTRA_PARAM_TELCO, voucherGameExtraParam)
            intent.putExtra(EXTRA_PARAM_OPERATOR_DATA, voucherGameOperatorAttributes)
            return intent
        }

    }
}