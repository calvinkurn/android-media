package com.tokopedia.home.test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.home.test.R

class HomeActivityTest : AppCompatActivity(), MainParentStatusBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_test)
    }

    override fun requestStatusBarLight() {

    }

    override fun requestStatusBarDark() {

    }

    fun setupFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .add(R.id.container_home, fragment, "TAG_FRAGMENT")
                .commit()
    }
}