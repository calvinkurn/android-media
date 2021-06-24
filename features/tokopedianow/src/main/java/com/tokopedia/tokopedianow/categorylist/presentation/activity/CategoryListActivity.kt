package com.tokopedia.tokopedianow.categorylist.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.presentation.fragment.CategoryListFragment

class CategoryListActivity: BaseActivity() {

    companion object {
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_category_list)

        val warehouseId = intent?.data?.getQueryParameter(PARAM_WAREHOUSE_ID).orEmpty()
        val fragment = CategoryListFragment.newInstance(warehouseId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
