package com.tokopedia.power_merchant.subscribe.view.fragment

import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.power_merchant.subscribe.R

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationFragment : BaseFragment() {

    override fun getResLayout(): Int = R.layout.fragment_pm_registration

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {

    }
}