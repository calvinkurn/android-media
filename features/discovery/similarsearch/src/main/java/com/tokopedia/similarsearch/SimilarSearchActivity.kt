package com.tokopedia.similarsearch

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

internal class SimilarSearchActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return SimilarSearchFragment.getInstance()
    }
}