package com.tokopedia.notifcenter.listener

interface NotificationFilterListener {
    fun updateFilter(filter: HashMap<String, Int>)
    fun sentFilterAnalytic(analyticData: String)
    fun isHasNotification(): Boolean
}