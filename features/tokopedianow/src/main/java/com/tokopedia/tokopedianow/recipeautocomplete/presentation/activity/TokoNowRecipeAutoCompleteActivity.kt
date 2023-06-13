package com.tokopedia.tokopedianow.recipeautocomplete.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipeautocomplete.presentation.fragment.TokoNowRecipeAutoCompleteFragment

class TokoNowRecipeAutoCompleteActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowRecipeAutoCompleteFragment.newInstance()
    }
}