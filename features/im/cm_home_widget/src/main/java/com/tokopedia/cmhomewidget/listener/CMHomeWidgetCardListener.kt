package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard

@CMHomeWidgetScope
interface CMHomeWidgetCardListener {
    fun onCardClick(cmHomeWidgetCard: CMHomeWidgetCard)
}