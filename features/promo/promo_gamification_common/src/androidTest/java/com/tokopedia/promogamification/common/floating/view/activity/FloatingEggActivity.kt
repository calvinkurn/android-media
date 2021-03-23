package com.tokopedia.promogamification.common.floating.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.promogamification.common.floating.view.fragment.FloatingEggButtonFragment


class FloatingEggActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.promogamification.common.test.R.layout.activity_floating)
//        val floatingEggButtonFragment =  supportFragmentManager.findFragmentById(com.tokopedia.promogamification.common.test.R.id.floating_egg_fragment) as FloatingEggButtonFragment
//        floatingEggButtonFragment.loadEggData()
    }
}