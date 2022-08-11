package com.tokopedia.tokopedianow.recipesearch.searchingredient.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipesearch.searchingredient.presentation.fragment.TokoNowRecipeSearchIngredientFragment

class TokoNowRecipeSearchIngredientActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment {
        return TokoNowRecipeSearchIngredientFragment.newInstance()
    }
}