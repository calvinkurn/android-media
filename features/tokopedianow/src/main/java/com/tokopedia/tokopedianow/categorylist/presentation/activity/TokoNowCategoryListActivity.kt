package com.tokopedia.tokopedianow.categorylist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.categorylist.presentation.fragment.TokoNowCategoryListFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

class TokoNowCategoryListActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowCategoryListFragment.newInstance()
    }
}
