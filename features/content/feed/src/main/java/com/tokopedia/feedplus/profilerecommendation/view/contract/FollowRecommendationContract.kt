package com.tokopedia.feedplus.profilerecommendation.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel

/**
 * Created by jegul on 2019-09-11.
 */
interface FollowRecommendationContract {

    interface View : CustomerView {

        fun onGetFollowRecommendationList(recomList: List<RecommendationCardViewModel>, cursor: String)

        fun onGetFollowRecommendationInfo(infoViewModel: FollowRecommendationInfoViewModel)
    }

    interface Presenter {

        fun getFollowRecommendationList(idList: List<Int>, cursor: String)
    }
}