package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

object TokoNowServerErrorUiModel: Visitable<TokoNowTypeFactory> {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}