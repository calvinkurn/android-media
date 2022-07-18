package com.tokopedia.home.beranda.listener

import android.view.View

/**
 * Created by dhaba
 */
interface BalanceWidgetListener {
    var gopayView : View?
    fun getRewardsView() : View
    fun getSubscriptionsView() : View
}