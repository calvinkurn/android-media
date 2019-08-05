package com.tokopedia.feedcomponent.view.viewmodel.mention

/**
 * Created by jegul on 2019-08-05.
 */

data class MentionableUserViewModel(
        val id: String,
        val userName: String,
        val fullName: String,
        val imageUrl: String
) {

    override fun toString(): String {
        return "{@$id|$fullName@}"
    }
}