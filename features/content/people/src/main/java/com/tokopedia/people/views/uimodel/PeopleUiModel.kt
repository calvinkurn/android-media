package com.tokopedia.people.views.uimodel

import com.tokopedia.library.baseadapter.BaseItem

/**
 * Created by meyta.taliti on 07/03/23.
 */
sealed class PeopleUiModel : BaseItem() {

    data class ShopUiModel(
        val id: String,
        val logoUrl: String,
        val badgeUrl: String,
        val name: String,
        val isFollowed: Boolean,
        val appLink: String,
    ): PeopleUiModel()

    data class UserUiModel(
        val id: String,
        val encryptedId: String,
        val photoUrl: String,
        val name: String,
        val username: String,
        val isFollowed: Boolean,
        val isMySelf: Boolean,
        val appLink: String,
    ): PeopleUiModel()
}
