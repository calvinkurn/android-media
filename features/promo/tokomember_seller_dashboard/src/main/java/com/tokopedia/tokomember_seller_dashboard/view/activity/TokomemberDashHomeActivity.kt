package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.HomeFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment.Companion.TAG_HOME
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramDetailFragment
import com.tokopedia.unifycomponents.TabsUnify

class TokomemberDashHomeActivity : AppCompatActivity(), HomeFragmentCallback {

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomember_dash_home)
        addFragment(TokomemberDashHomeMainFragment.newInstance(intent.extras), TAG_HOME)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.findFragmentByTag(TAG_HOME)
            supportFragmentManager.popBackStack()
        }
        else if(supportFragmentManager.backStackEntryCount == 1){
            supportFragmentManager.popBackStack()
            return super.onBackPressed()
        }
        else {
            return super.onBackPressed()
        }
    }

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_home, fragment, tag)
            .addToBackStack(tag).commit()
    }

    override fun addFragment() {
        addFragment(TokomemberDashProgramDetailFragment.newInstance(intent.extras), TAG_HOME)
    }
}