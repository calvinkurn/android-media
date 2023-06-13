package com.tokopedia.mvc.presentation.intro.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.mvc.presentation.intro.adapter.factory.MvcIntroAdapterFactory

data class VoucherIntroCarouselUiModel(
    val headerTitle: String,
    val tabsList: List<VoucherIntroTabsData> = emptyList()
) : Visitable<MvcIntroAdapterFactory> {
    override fun type(typeFactory: MvcIntroAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class VoucherIntroTabsData(
        val tabHeader: String,
        val tabDescription: String,
        val listOfImages: List<String> = emptyList()
    )
}
