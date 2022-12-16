package com.tokopedia.mvc.presentation.intro.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.mvc.presentation.intro.adapter.factory.MvcIntroAdapterFactory

data class IntroVoucherUiModel(
    val title: String,
    val subtitle: String,
    val list: List<IntroCouponCardData> = emptyList()
) : Visitable<MvcIntroAdapterFactory> {
    override fun type(typeFactory: MvcIntroAdapterFactory): Int {
        return typeFactory.type(this)
    }
    data class IntroCouponCardData(
        val benefitTitle: String = "",
        val benefitSubtitle: String = "",
        val benefitImageUrl: String = ""
    )
}
