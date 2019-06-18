package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel

class ProfileListViewModelMapper : Mapper<SearchProfileModel, ProfileListViewModel> {

    override fun convert(source: SearchProfileModel): ProfileListViewModel {
        val profileListViewModel = ArrayList<ProfileViewModel>()

        if(source.aceSearchProfile?.profiles == null)
            return ProfileListViewModel(listOf(), false, 0)

        val profileListModel = source.aceSearchProfile
        for (item in profileListModel.profiles) {
            val profileViewModel = ProfileViewModel(
                item.id,
                item.name,
                item.avatar,
                item.username,
                item.followed,
                item.iskol,
                item.isaffiliate,
                item.followers,
                item.postCount
            )

            profileListViewModel.add(profileViewModel)
        }

        return ProfileListViewModel(
            profileListViewModel,
            source.aceSearchProfile.hasNext,
            source.aceSearchProfile.count
        )
    }
}