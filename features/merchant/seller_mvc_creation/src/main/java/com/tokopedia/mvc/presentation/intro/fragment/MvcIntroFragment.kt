package com.tokopedia.mvc.presentation.intro.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

class MvcIntroFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): MvcIntroFragment {
            return MvcIntroFragment()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }
}
