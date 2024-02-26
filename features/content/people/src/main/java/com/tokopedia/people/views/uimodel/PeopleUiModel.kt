package com.tokopedia.people.views.uimodel

import androidx.compose.runtime.Immutable
import com.tokopedia.library.baseadapter.BaseItem

/**
 * Created by meyta.taliti on 07/03/23.
 */
sealed class PeopleUiModel : BaseItem() {

    @Immutable
    data class ShopUiModel(
        val id: String,
        val logoUrl: String,
        val badgeUrl: String,
        val name: String,
        val isFollowed: Boolean,
        val appLink: String
    ) : PeopleUiModel()

    @Immutable
    data class UserUiModel(
        val id: String,
        val encryptedId: String,
        val photoUrl: String,
        val name: String,
        val username: String,
        val isFollowed: Boolean,
        val isMySelf: Boolean,
        val appLink: String
    ) : PeopleUiModel()
}

val PeopleUiModel.id: String
    get() = when (this) {
        is PeopleUiModel.ShopUiModel -> id
        is PeopleUiModel.UserUiModel -> id
    }

val PeopleUiModel.appLink: String
    get() = when (this) {
        is PeopleUiModel.ShopUiModel -> appLink
        is PeopleUiModel.UserUiModel -> appLink
    }

val PeopleUiModel.isMySelf: Boolean
    get() = when (this) {
        is PeopleUiModel.ShopUiModel -> false
        is PeopleUiModel.UserUiModel -> isMySelf
    }

val PeopleUiModel.isFollowed: Boolean
    get() = when (this) {
        is PeopleUiModel.ShopUiModel -> isFollowed
        is PeopleUiModel.UserUiModel -> isFollowed
    }

fun PeopleUiModel.setIsFollowed(shouldFollow: Boolean): PeopleUiModel {
    return when (this) {
        is PeopleUiModel.UserUiModel -> copy(isFollowed = shouldFollow)
        is PeopleUiModel.ShopUiModel -> copy(isFollowed = shouldFollow)
    }
}
