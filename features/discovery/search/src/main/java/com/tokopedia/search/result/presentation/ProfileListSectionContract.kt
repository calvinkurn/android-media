package com.tokopedia.search.result.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel

interface ProfileListSectionContract {

    interface View : CustomerView {
        fun getBaseAppComponent() : BaseAppComponent
        fun onSuccessToggleFollow(adapterPosition : Int, enable : Boolean)
        fun onErrorToggleFollow()
        fun onErrorToggleFollow(errorMessage : String)
        fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel)
        fun onErrorGetProfileListData()
        fun renderVisitableList(visitableList: List<Visitable<*>>)
        fun clearVisitableList()
        fun trackEmptySearchProfile()
        fun hideLoading()
        fun trackImpressionRecommendationProfile(recommendationProfileTrackingObjectList: List<Any>)
        fun trackClickProfile(profileViewModel: ProfileViewModel)
        fun trackClickRecommendationProfile(profileViewModel: ProfileViewModel)
        fun route(applink: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun initInjector()
        fun requestProfileListData(query: String, page: Int)
        fun handleFollowAction(adapterPosition: Int,
                               userToFollowId: Int,
                               followedStatus: Boolean)
        fun getNextPage(): Int
        fun getHasNextPage(): Boolean
        fun getTotalProfileCount(): Int
        fun onViewClickProfile(profileViewModel: ProfileViewModel)
    }
}