package com.tokopedia.mvc.presentation.intro

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.presentation.intro.fragment.MvcIntroFragment

class MvcIntroActivity : BaseSimpleActivity() {
    override fun getNewFragment() = MvcIntroFragment()
}
