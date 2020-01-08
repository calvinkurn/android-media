package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel

class ProfileListViewModelMapper : Mapper<SearchProfileModel, ProfileListViewModel> {

    override fun convert(source: SearchProfileModel): ProfileListViewModel {
        source.aceSearchProfile?.profiles ?: return ProfileListViewModel()

        val aceSearchProfile = source.aceSearchProfile
        val profileListViewModel = convertToProvileViewModelList(aceSearchProfile.profiles, false)
        val recommendationProfileListViewModel = convertToProvileViewModelList(aceSearchProfile.topProfile, true)

        return ProfileListViewModel(
            profileListViewModel,
            recommendationProfileListViewModel,
            source.aceSearchProfile.hasNext,
            source.aceSearchProfile.count
        )
    }

    private fun convertToProvileViewModelList(
            profileList: List<SearchProfileModel.Profile>,
            isRecommendation: Boolean
    ): List<ProfileViewModel> {
        val profileListViewModel = mutableListOf<ProfileViewModel>()

        profileList.forEach { profile ->
            val profileViewModel = convertToProfileViewModel(profile, isRecommendation)
            profileListViewModel.add(profileViewModel)
        }

        return profileListViewModel
    }

    private fun convertToProfileViewModel(profile: SearchProfileModel.Profile, isRecommendation: Boolean): ProfileViewModel {
        return ProfileViewModel(
                profile.id,
                profile.name,
                profile.avatar,
                profile.username,
                profile.followed,
                profile.iskol,
                profile.isaffiliate,
                profile.followers,
                profile.postCount,
                isRecommendation
        )
    }
}