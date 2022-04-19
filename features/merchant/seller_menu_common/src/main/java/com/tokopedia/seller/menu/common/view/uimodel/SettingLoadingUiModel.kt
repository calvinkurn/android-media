package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

object SettingLoadingUiModel: SettingUiModel {

    override val onClickApplink: String? = null

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

}