package com.tokopedia.tokopedianow.recentpurchase.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recentpurchase.presentation.fragment.TokoNowRecentPurchaseFragment

class TokoNowRecentPurchaseActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment = TokoNowRecentPurchaseFragment.newInstance()
}