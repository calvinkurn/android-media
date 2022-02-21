package com.tokopedia.logisticCommon.ui.userconsent

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TncWebViewActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return TncWebViewFragment.createInstance()
    }
}