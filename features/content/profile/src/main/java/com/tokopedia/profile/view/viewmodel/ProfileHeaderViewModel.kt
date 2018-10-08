package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * @author by milhamj on 9/20/18.
 */
data class ProfileHeaderViewModel(
        val name: String = "",
        val avatar: String = "",
        val followers: String = "0",
        val following: String = "0",
        val recommendationQuota: String = "",
        val userId: Int = 0,
        val isKol: Boolean = false,
        val isFollowed: Boolean = false,
        val isOwner: Boolean = false) : Visitable<ProfileTypeFactory> {

    override fun type(typeFactory: ProfileTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}