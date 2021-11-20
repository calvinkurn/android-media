package com.tokopedia.cmhomewidget.communicator

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener

interface CMHomeWidgetCommunicator {
    fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData)
    fun hideCMHomeWidget()
    fun showCMHomeWidget()
    fun setOnCMHomeWidgetCloseClickListener(cmHomeWidgetCloseClickListener: CMHomeWidgetCloseClickListener)
    fun removeCMHomeWidgetDismissListener()
}