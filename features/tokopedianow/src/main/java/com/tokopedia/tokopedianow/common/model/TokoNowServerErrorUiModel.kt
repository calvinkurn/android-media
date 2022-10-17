package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory

object TokoNowServerErrorUiModel: Visitable<TokoNowServerErrorTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowServerErrorTypeFactory): Int {
        return typeFactory.type(this)
    }
}