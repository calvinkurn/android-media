package com.tokopedia.search.result.presentation.model

open class ProfileListViewModel(val profileModelList: List<ProfileViewModel> = listOf(),
                                val recommendationProfileModelList: List<ProfileViewModel> = listOf(),
                                val isHasNextPage: Boolean = false,
                                val totalSearchCount: Int = 0) {

    fun getListTrackingObject() : List<Any> {
        val listTracking = arrayListOf<Any>()

        for(profileModel in profileModelList) {
            listTracking.add(profileModel.getTrackingObject())
        }

        return listTracking
    }

    fun getRecommendationListTrackingObject() : List<Any> {
        val listTracking = arrayListOf<Any>()

        for(profileModel in recommendationProfileModelList) {
            listTracking.add(profileModel.getRecommendationTrackingObject())
        }

        return listTracking
    }
}
