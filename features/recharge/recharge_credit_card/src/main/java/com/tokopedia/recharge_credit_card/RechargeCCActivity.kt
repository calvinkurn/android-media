package com.tokopedia.recharge_credit_card

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import kotlinx.android.synthetic.main.activity_recharge_cc.*

/*
 * applink production = tokopedia://digital/form?category_id=26&menu_id=169&template=tagihancc
 * applink staging = tokopedia://digital/form?category_id=26&menu_id=86&template=tagihancc
 * for activating staging, dont forget change base url on submit PCIDSS
 */

class RechargeCCActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID, CATEGORY_ID_DEFAULT)?: CATEGORY_ID_DEFAULT
        val menuId = bundle?.getString(PARAM_MENU_ID, MENU_ID_DEFAULT)?: MENU_ID_DEFAULT
        return RechargeCCFragment.newInstance(categoryId, menuId)
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
        setSecureWindowFlag()

        toolbar_credit_card.addRightIcon(com.tokopedia.common_digital.R.drawable.digital_common_ic_tagihan)
        toolbar_credit_card.rightIcons?.let {
            it[0].setOnClickListener {
                RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
            }
        }
    }

    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) }
        }
    }


    companion object {
        private const val PARAM_MENU_ID = "menu_id"
        private const val PARAM_CATEGORY_ID = "category_id"

        private const val CATEGORY_ID_DEFAULT = "26"
        private const val MENU_ID_DEFAULT = "169"
    }
}