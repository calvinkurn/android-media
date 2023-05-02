package com.tokopedia.tokopedianow.seeallcategory.persentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.seeallcategory.persentation.fragment.TokoNowSeeAllCategoryFragment

class TokoNowSeeAllCategoryActivity: BaseTokoNowActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun getFragment(): Fragment {
        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        return TokoNowSeeAllCategoryFragment.newInstance(warehouseId)
    }
}
