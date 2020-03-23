package com.tokopedia.test.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.di.module.ApiModuleTest
import com.tokopedia.home.test.R
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener

internal class HomeActivityTest : AppCompatActivity(), MainParentStatusBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_test)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_home, getNewFragment(), "TAG_FRAGMENT")
                .commit()
    }

    private fun getNewFragment(): Fragment {
        return object: HomeFragment(){
            override fun initInjector() {
                val component = DaggerBerandaComponent.builder()
                        .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                        .apiModule(ApiModuleTest())
                        .build()
                component.inject(this)
            }
        }
    }

    override fun requestStatusBarLight() {

    }

    override fun requestStatusBarDark() {

    }
}