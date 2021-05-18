package com.tokopedia.mvcwidget.views

import com.tokopedia.mvcwidget.MvcSource

interface MvcDetailViewContract {
    fun handleFollowButtonClick()
    fun handleJadiMemberButtonClick()
    fun getWidgetImpression():WidgetImpression
    fun getShopId():String
    fun handleCollapseExpand(adapterPosition: Int)
    @MvcSource fun getMvcSource():Int
}