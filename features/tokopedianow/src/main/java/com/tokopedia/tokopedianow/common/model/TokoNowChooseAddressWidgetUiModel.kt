package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE_TRACKING

data class TokoNowChooseAddressWidgetUiModel(
    val id: String = "",
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY,
    val trackingSource: String = SOURCE_TRACKING,
    val eventLabelHostPage: String = ""
) : Visitable<TokoNowChooseAddressWidgetTypeFactory>  {
    override fun type(typeFactory: TokoNowChooseAddressWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
