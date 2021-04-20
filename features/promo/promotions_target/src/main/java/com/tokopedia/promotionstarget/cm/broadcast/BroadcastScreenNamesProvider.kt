package com.tokopedia.promotionstarget.cm.broadcast

class BroadcastScreenNamesProvider {
    private val DISCO_PAGE_ACTIVITY_NAME = "com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity"
    private val screenNameList = arrayListOf(DISCO_PAGE_ACTIVITY_NAME)

    fun screenNames(): ArrayList<String> {
        return screenNameList
    }

}