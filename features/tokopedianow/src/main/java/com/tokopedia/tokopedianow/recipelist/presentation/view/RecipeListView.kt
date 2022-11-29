package com.tokopedia.tokopedianow.recipelist.presentation.view

import android.content.Context
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel

interface RecipeListView {

    fun viewModel(): BaseTokoNowRecipeListViewModel

    fun fragment(): BaseTokoNowRecipeListFragment

    fun context(): Context?
}