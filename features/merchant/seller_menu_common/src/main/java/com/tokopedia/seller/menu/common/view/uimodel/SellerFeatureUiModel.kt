package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.user.session.UserSessionInterface

class SellerFeatureUiModel(val userSession: UserSessionInterface) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

    override val onClickApplink: String?
        get() = null
}