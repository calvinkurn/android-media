package com.tokopedia.topupbills.telco.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topupbills.R

open abstract class BaseTelcoActivity : BaseSimpleActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_telco
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_telco
    }

    companion object {
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
    }
}