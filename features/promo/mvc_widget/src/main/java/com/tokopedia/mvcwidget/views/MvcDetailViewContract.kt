package com.tokopedia.mvcwidget.views

import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker

interface MvcDetailViewContract {
    fun handleFollowButtonClick()
    fun handleJadiMemberButtonClick()
    fun getWidgetImpression():WidgetImpression
    fun getMvcTracker():MvcTracker?
    fun getShopId():String
    fun getProductId():String
    @MvcSource fun getMvcSource():Int
}