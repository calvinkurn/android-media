package com.tokopedia.tokomart.categorylist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tokomart.categorylist.presentation.fragment.TokoMartCategoryListFragment

class TokoMartCategoryListActivity: BaseSimpleActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun getNewFragment(): Fragment {
        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        return TokoMartCategoryListFragment.newInstance(warehouseId)
    }
}