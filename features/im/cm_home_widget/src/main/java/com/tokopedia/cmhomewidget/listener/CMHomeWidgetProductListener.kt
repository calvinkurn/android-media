package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProduct

@CMHomeWidgetScope
interface CMHomeWidgetProductListener {
    fun onProductClick(item: CMHomeWidgetProduct)
}