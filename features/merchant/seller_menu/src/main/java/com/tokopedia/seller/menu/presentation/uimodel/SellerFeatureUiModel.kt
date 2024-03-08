package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory
import com.tokopedia.user.session.UserSessionInterface

class SellerFeatureUiModel(val userSession: UserSessionInterface) :
    SellerMenuItem {

    override fun type(typeFactory: SellerMenuTypeFactory): Int =
        typeFactory.type(this)
}
