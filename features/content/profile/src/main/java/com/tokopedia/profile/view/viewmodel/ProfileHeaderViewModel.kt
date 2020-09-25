package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * @author by milhamj on 9/20/18.
 */
data class ProfileHeaderViewModel(
        val name: String = "",
        val avatar: String = "",
        var followers: String = ZERO,
        var following: String = ZERO,
        val affiliateName: String = "",
        val link: String = "",
        val userId: Int = 0,
        val isKol: Boolean = false,
        val isAffiliate: Boolean = false,
        var isShowAffiliateContent: Boolean = false,
        var isFollowed: Boolean = false,
        val isOwner: Boolean = false,
        val isCreatePostToggleOn: Boolean = false
) : Visitable<ProfileTypeFactory> {

    companion object {
        const val ZERO = "0"
    }

    val formattedAffiliateName: String
        get() = "@$affiliateName"

    override fun type(typeFactory: ProfileTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}