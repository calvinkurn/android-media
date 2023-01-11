package com.tokopedia.tokopedianow.categorymenu.persentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.categorymenu.persentation.fragment.TokoNowSeeAllCategoriesFragment

class TokoNowSeeAllCategoriesActivity: BaseTokoNowActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun getFragment(): Fragment {
        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        return TokoNowSeeAllCategoriesFragment.newInstance(warehouseId)
    }
}
