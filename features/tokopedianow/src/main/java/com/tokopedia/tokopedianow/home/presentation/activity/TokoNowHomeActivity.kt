package com.tokopedia.tokopedianow.home.presentation.activity

import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment

class TokoNowHomeActivity : BaseTokoNowActivity() {

    override fun getFragment() = TokoNowHomeFragment.newInstance()
}