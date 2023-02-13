package com.tokopedia.tokopedianow.recipehome.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeHomeFragment

class TokoNowRecipeHomeActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowRecipeHomeFragment.newInstance()
    }
}