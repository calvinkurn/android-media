package com.tokopedia.tokopedianow.categorylist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.categorylist.presentation.fragment.TokoNowCategoryListFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

class TokoNowCategoryListActivity: BaseTokoNowActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun getFragment(): Fragment {
        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        return TokoNowCategoryListFragment.newInstance(warehouseId)
    }
}
