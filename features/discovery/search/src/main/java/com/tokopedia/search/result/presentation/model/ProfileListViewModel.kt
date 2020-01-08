package com.tokopedia.search.result.presentation.model

open class ProfileListViewModel(val profileModelList: List<ProfileViewModel> = listOf(),
                                val recommendationProfileModelList: List<ProfileViewModel> = listOf(),
                                val isHasNextPage: Boolean = false,
                                val totalSearchCount: Int = 0) {

    fun getListTrackingObject() : List<Any> {
        return profileModelList.createTrackingListFromProfileViewModelList {
            it.getTrackingObject()
        }
    }

    fun getRecommendationListTrackingObject() : List<Any> {
        return recommendationProfileModelList.createTrackingListFromProfileViewModelList {
            it.getRecommendationTrackingObject()
        }
    }

    private fun List<ProfileViewModel>.createTrackingListFromProfileViewModelList(
            trackingObject: (profileViewModel: ProfileViewModel) -> Any
    ): List<Any> {
        val listTracking = arrayListOf<Any>()

        for(profileModel in this) {
            listTracking.add(trackingObject)
        }

        return listTracking
    }
}
