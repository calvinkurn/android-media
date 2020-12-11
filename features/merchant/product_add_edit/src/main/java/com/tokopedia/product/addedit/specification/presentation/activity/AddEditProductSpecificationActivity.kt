package com.tokopedia.product.addedit.specification.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.specification.presentation.fragment.AddEditProductSpecificationFragment

class AddEditProductSpecificationActivity: BaseSimpleActivity() {

    companion object {
        fun createInstance(context: Context?, cacheManagerId: String?): Intent =
                Intent(context, AddEditProductSpecificationActivity::class.java)
                        .putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
    }

    override fun getLayoutRes() = com.tokopedia.product.addedit.R.layout.activity_add_edit_product_specification

    override fun getParentViewResourceID(): Int = com.tokopedia.product.addedit.R.id.parent_view

    override fun getNewFragment(): Fragment {
        val cacheManagerId = intent?.getStringExtra(EXTRA_CACHE_MANAGER_ID).orEmpty()
        return AddEditProductSpecificationFragment.createInstance(cacheManagerId)
    }
}
