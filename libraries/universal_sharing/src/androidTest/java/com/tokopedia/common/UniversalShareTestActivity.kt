package com.tokopedia.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

abstract class UniversalShareTestActivity : BaseSimpleActivity() {

    lateinit var shareFragmentTest: UniversalShareFragmentTest


    override fun onCreate(savedInstanceState: Bundle?) {
        if (::shareFragmentTest.isInitialized.not()) throw Exception("please set share fragment")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_universal_share_test)
    }

    override fun getNewFragment(): Fragment? {
        return getShareFragment()
    }

    fun getShareFragment(): UniversalShareFragmentTest = shareFragmentTest

    fun setShareFragment(fragment: UniversalShareFragmentTest) {
        shareFragmentTest = fragment
    }
}
