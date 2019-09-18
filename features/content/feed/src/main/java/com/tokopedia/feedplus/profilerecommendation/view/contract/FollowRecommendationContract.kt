package com.tokopedia.feedplus.profilerecommendation.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecommendationAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel

/**
 * Created by jegul on 2019-09-11.
 */
interface FollowRecommendationContract {

    interface View : CustomerView {

        fun onGetFollowRecommendationList(recomList: List<FollowRecommendationCardViewModel>, cursor: String)

        fun onGetFollowRecommendationInfo(infoViewModel: FollowRecommendationInfoViewModel)

        fun onSuccessFollowRecommendation(id: String)

        fun onSuccessUnfollowRecommendation(id: String)

        fun onSuccessFollowAllRecommendation()

        fun onFinishSetOnboardingStatus()

        fun onGetError(error: Throwable)

        fun onGetError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {

        fun getFollowRecommendationList(interestIds: IntArray, cursor: String)

        fun followAllRecommendation(interestIds: IntArray)

        fun followUnfollowRecommendation(id: String, action: FollowRecommendationAction)

        fun setOnboardingStatus()
    }
}