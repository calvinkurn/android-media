package com.tokopedia.tokopedianow.repurchase.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment

class TokoNowRepurchaseActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment = TokoNowRepurchaseFragment.newInstance()
}