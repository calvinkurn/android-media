package com.tokopedia.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

class UniversalShareTestActivity : BaseSimpleActivity() {

    private val shareFragmentTest = UniversalShareFragmentTest()
    
    override fun getNewFragment(): Fragment {
        return getShareFragment()
    }

    fun getShareFragment(): UniversalShareFragmentTest = shareFragmentTest
}
