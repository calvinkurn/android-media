package com.tokopedia.mvc.presentation.intro.adapter.factory

import com.tokopedia.mvc.presentation.intro.uimodel.IntroCouponUiModel

interface MvcIntroAdapterFactory {
    fun type(introCouponUiModel: IntroCouponUiModel): Int
}
