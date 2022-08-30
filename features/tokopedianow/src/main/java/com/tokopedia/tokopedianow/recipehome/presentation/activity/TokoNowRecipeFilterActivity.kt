package com.tokopedia.tokopedianow.recipehome.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeFilterFragment
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeFilterFragment.Companion.EXTRA_SELECTED_FILTER_IDS

class TokoNowRecipeFilterActivity : BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        val selectedFilterIds = intent
            .getStringArrayListExtra(EXTRA_SELECTED_FILTER_IDS).orEmpty()
        return TokoNowRecipeFilterFragment.newInstance(selectedFilterIds)
    }
}