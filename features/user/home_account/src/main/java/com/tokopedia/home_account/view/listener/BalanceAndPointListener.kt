package com.tokopedia.home_account.view.listener

interface BalanceAndPointListener {
    fun onClickBalanceAndPoint(id: String, applink: String?, weblink: String?, isFailed: Boolean, isActive: Boolean)
    fun onZeroCounter()
}