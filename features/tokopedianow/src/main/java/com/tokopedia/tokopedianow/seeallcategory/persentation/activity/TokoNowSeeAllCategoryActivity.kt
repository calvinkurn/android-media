package com.tokopedia.tokopedianow.seeallcategory.persentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.seeallcategory.persentation.fragment.TokoNowSeeAllCategoryFragment

class TokoNowSeeAllCategoryActivity : BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowSeeAllCategoryFragment.newInstance()
    }
}
