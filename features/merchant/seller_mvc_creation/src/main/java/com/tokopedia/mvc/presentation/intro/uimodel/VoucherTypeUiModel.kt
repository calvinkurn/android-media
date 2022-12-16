package com.tokopedia.mvc.presentation.intro.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.mvc.presentation.intro.adapter.factory.MvcIntroAdapterFactory

data class VoucherTypeUiModel(
    val title: String,
    val list: List<VoucherIntroTypeData> = emptyList()
) : Visitable<MvcIntroAdapterFactory> {
    override fun type(typeFactory: MvcIntroAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
