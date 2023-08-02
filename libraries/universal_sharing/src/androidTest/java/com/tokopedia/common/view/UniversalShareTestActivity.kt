package com.tokopedia.common.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class UniversalShareTestActivity : BaseSimpleActivity() {

    private val shareFragmentTest = UniversalShareFragmentTest()
    
    override fun getNewFragment(): Fragment {
        return getShareFragment()
    }

    fun getShareFragment(): UniversalShareFragmentTest = shareFragmentTest
}
