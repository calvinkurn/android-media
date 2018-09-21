package com.tokopedia.profile.view.viewmodel

/**
 * @author by milhamj on 9/21/18.
 */
data class ProfileFirstPageViewModel(
        val profileHeaderViewModel: ProfileHeaderViewModel = ProfileHeaderViewModel(),
        val profilePostViewModel: List<ProfilePostViewModel> = ArrayList()
)