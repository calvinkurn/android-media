package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.ProfileViewModel

interface ProfileListener {
    fun onFollowButtonClicked(adapterPosition: Int, profileModel: ProfileViewModel)
    fun onHandleProfileClick(profileModel: ProfileViewModel)
}