package com.tokopedia.tokopedianow.recipedetail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeDetailFragment

class TokoNowRecipeDetailActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowRecipeDetailFragment.newInstance()
    }
}