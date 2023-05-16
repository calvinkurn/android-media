package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory

class TokoNowProgressBarUiModel: Visitable<TokoNowProgressBarTypeFactory> {
    override fun type(typeFactory: TokoNowProgressBarTypeFactory): Int = typeFactory.type(this)
}
