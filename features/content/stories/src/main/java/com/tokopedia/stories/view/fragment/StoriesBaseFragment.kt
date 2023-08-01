package com.tokopedia.stories.view.fragment

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import javax.inject.Inject

class StoriesBaseFragment @Inject constructor(): TkpdBaseV4Fragment() {

    override fun getScreenName(): String {
        return "Screen"
    }

}
