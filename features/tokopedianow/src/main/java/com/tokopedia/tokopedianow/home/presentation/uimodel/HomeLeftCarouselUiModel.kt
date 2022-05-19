package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeLeftCarouselUiModel(
    val id : String,
    val name :String,
    val header: TokoNowDynamicHeaderUiModel,
    val productList: List<Visitable<*>> = listOf(),
    val backgroundColorArray: ArrayList<String> = arrayListOf(),
    val campaignId: String = "",
    var imageBanner: String = "",
    var imageBannerAppLink: String = "",
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}