package com.tokopedia.tokomart.categorylist.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.fragment.TokoMartCategoryListFragment

class TokoMartCategoryListActivity: BaseActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toko_now_category_list)

        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        val fragment = TokoMartCategoryListFragment.newInstance(warehouseId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
