package com.tokopedia.tokopedianow.oldrecipebookmark.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.fragment.TokoNowRecipeBookmarkFragment

class TokoNowRecipeBookmarkActivity : BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        return TokoNowRecipeBookmarkFragment.newInstance()
    }
}
