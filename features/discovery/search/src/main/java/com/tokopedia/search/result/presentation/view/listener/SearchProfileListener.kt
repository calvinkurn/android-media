package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.ProfileListViewModel

interface SearchProfileListener {
    fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel)
    fun onErrorGetProfileListData()
}