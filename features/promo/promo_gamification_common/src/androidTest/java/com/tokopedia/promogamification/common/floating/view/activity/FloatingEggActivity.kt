package com.tokopedia.promogamification.common.floating.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity


class FloatingEggActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.promogamification.common.test.R.layout.activity_floating)
    }
}