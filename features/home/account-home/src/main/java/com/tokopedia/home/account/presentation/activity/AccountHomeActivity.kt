package com.tokopedia.home.account.presentation.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil.setWindowFlag
import kotlinx.android.synthetic.main.activity_account_home.*


/**
 * Created by Yoris Prayogo on 01/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AccountHomeActivity: FragmentActivity() {

//    override fun getNewFragment(): Fragment? {
//        return AccountHomeFragment.newInstance(Bundle())
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_home)
//        setupStatusBar()
        setFragment()
    }

    private fun setFragment(){
        val newFragment: Fragment = AccountHomeFragment.newInstance(Bundle())

        supportFragmentManager.beginTransaction()
                .replace(R.id.account_parent_activity, newFragment, "")
                .commit()

    }

//    fun setupStatusBar() {
//        //apply inset to allow recyclerview scrolling behind status bar
//        account_parent_activity?.setFitsSystemWindows(false)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            account_parent_activity?.requestApplyInsets()
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags: Int = account_parent_activity?.getSystemUiVisibility() ?: 0
//            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            account_parent_activity?.setSystemUiVisibility(flags)
//            window.statusBarColor = Color.WHITE
//        }
//
//        //make full transparent statusBar
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            window.statusBarColor = Color.TRANSPARENT
//        }
//    }

}