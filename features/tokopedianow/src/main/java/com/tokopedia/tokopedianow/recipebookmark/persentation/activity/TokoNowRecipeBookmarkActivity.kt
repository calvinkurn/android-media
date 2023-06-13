package com.tokopedia.tokopedianow.recipebookmark.persentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipebookmark.persentation.fragment.TokoNowRecipeBookmarkFragment

class TokoNowRecipeBookmarkActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowRecipeBookmarkFragment.newInstance()
    }
}