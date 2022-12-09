package com.tokopedia.feedcomponent.view.viewmodel.mention

/**
 * Created by jegul on 2019-08-05.
 */

data class MentionableUserModel(
        val id: String,
        val userName: String?,
        val fullName: String,
        val avatarUrl: String?,
        val isShop: Boolean = false
) {

    override fun toString(): String {
        return "{@$id|$fullName@}"
    }
}
