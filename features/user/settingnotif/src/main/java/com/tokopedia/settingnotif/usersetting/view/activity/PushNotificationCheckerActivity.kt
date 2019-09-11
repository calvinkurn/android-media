package com.tokopedia.settingnotif.usersetting.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingnotif.usersetting.view.fragment.PushNotifCheckerFragment

class PushNotificationCheckerActivity: BaseSimpleActivity() {

    override fun getNewFragment() = PushNotifCheckerFragment()

}