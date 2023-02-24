package com.tokopedia.people.views.adapter.listener

import com.tokopedia.people.views.uimodel.profile.ProfileUiModel

interface UserFollowListener {

    fun onItemUserClicked(model: ProfileUiModel.UserUiModel, position: Int)

    fun onItemShopClicked(model: ProfileUiModel.ShopUiModel, position: Int)

    fun onFollowUserClicked(model: ProfileUiModel.UserUiModel, position: Int)

    fun onFollowShopClicked(model: ProfileUiModel.ShopUiModel, position: Int)

}
