package com.tokopedia.home.account.presentation.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.account.presentation.fragment.PushNotifCheckerFragment

class PushNotificationCheckerActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = PushNotifCheckerFragment()

}