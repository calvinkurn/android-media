package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.brandlist.brandlist_category.data.model.Category

class BrandlistFragment : BaseDaggerFragment() {

    companion object {
        const val KEY_CATEGORY = "BRAND_LIST_CATEGORY"
        @JvmStatic
        fun newInstance(bundle: Bundle?) = BrandlistFragment().apply { arguments = bundle }
    }

    private var category: Category? = null
    private var isFirstDataLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(KEY_CATEGORY)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() { }

    private fun loadData(isRefresh: Boolean = false) {
        if (!isFirstDataLoaded || isRefresh) {

        }
    }
}