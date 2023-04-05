package com.tokopedia.people.views.adapter.listener

import com.tokopedia.people.views.uimodel.PeopleUiModel

interface UserFollowListener {

    fun onItemUserClicked(model: PeopleUiModel.UserUiModel, position: Int)

    fun onItemShopClicked(model: PeopleUiModel.ShopUiModel, position: Int)

    fun onFollowUserClicked(model: PeopleUiModel.UserUiModel, position: Int)

    fun onFollowShopClicked(model: PeopleUiModel.ShopUiModel, position: Int)

}
