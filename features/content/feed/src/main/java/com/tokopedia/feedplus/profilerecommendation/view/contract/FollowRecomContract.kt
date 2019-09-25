package com.tokopedia.feedplus.profilerecommendation.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecomAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomInfoViewModel

/**
 * Created by jegul on 2019-09-11.
 */
interface FollowRecomContract {

    interface View : CustomerView {

        fun onGetFollowRecommendationList(recomList: List<FollowRecomCardViewModel>, cursor: String)

        fun onGetFollowRecommendationInfo(infoViewModel: FollowRecomInfoViewModel)

        fun onSuccessFollowRecommendation(id: String)

        fun onSuccessUnfollowRecommendation(id: String)

        fun onSuccessFollowAllRecommendation()

        fun onFinishSetOnboardingStatus()

        fun onErrorSetOnboardingStatus(throwable: Throwable)

        fun onGetError(error: Throwable)

        fun onGetError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {

        fun getFollowRecommendationList(interestIds: IntArray, cursor: String)

        fun followAllRecommendation(interestIds: IntArray)

        fun followUnfollowRecommendation(id: String, action: FollowRecomAction)

        fun setOnboardingStatus()
    }
}