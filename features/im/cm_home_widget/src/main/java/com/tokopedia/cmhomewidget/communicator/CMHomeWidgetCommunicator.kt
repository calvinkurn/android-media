package com.tokopedia.cmhomewidget.communicator

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener

interface CMHomeWidgetCommunicator {
    fun onCMHomeWidgetDataReceived(cmHomeWidgetData: CMHomeWidgetData)
    fun hide()
    fun show()
    fun setOnCMHomeWidgetCloseClickListener(cmHomeWidgetCloseClickListener: CMHomeWidgetCloseClickListener)
    fun removeCMHomeWidgetDismissListener()
}