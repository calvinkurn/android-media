package com.tokopedia.cmhomewidget.domain.data

import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactory
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable

class CMHomeWidgetProductCardShimmerData : CMHomeWidgetVisitable {
    override fun type(typeFactory: CMHomeWidgetViewHolderTypeFactory): Int {
        return typeFactory.type(this)
    }
}