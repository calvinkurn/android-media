package com.tokopedia.profile.view.viewmodel

/**
 * @author by milhamj on 9/20/18.
 */
data class ProfileHeaderViewModel(
        val name: String = "",
        val avatar: String = "",
        val followers: String = "0",
        val following: String = "0",
        val isKol: Boolean = false,
        val isFollowed: Boolean = false
)