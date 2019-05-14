package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.auto.view.fragment.EmptyProductFragment

/**
 * Author errysuprayogi on 09,May,2019
 */
class EmptyProductActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return EmptyProductFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }
}
