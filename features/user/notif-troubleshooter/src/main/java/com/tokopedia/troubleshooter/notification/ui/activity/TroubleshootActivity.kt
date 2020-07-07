package com.tokopedia.troubleshooter.notification.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.troubleshooter.notification.ui.fragment.TroubleshootFragment

class TroubleshootActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return TroubleshootFragment()
    }

}