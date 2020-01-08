package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileRecommendationTitleViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

// This class is currently made 'public' due to Kotlin not having 'private package' access modifier
// Please avoid using this class directly, and use ProfileListSectionContract.Presenter instead
class ProfileListPresenter : BaseDaggerPresenter<ProfileListSectionContract.View>() , ProfileListSectionContract.Presenter {

    companion object {
        const val PARAM_USER_ID = "{user_id}"
    }

    @field:[Inject Named(SearchConstant.SearchProfile.SEARCH_PROFILE_USE_CASE)]
    lateinit var searchProfileListUseCase : UseCase<SearchProfileModel>

    @Inject
    lateinit var followKolPostGqlUseCase : FollowKolPostGqlUseCase

    @Inject
    lateinit var profileListViewModelMapper: Mapper<SearchProfileModel, ProfileListViewModel>

    private var nextPage = 0
    private var hasNextPage = false
    private var totalProfileCount = Integer.MAX_VALUE

    override fun initInjector() {
        val component = DaggerProfileListPresenterComponent.builder()
            .baseAppComponent(view?.getBaseAppComponent())
            .build()

        component.inject(this)
    }

    override fun getNextPage(): Int {
        return nextPage
    }

    override fun getHasNextPage(): Boolean {
        return hasNextPage
    }

    override fun getTotalProfileCount(): Int {
        return totalProfileCount
    }

    override fun handleFollowAction(adapterPosition: Int,
                                    userToFollowId: Int,
                                    followedStatus: Boolean) {
        val requestedAction : Int =
            when(followedStatus) {
                true -> FollowKolPostGqlUseCase.PARAM_UNFOLLOW
                false -> FollowKolPostGqlUseCase.PARAM_FOLLOW
            }

        followKolPostGqlUseCase.execute(
            FollowKolPostGqlUseCase.getParam(userToFollowId, requestedAction),
            getFollowKolSubscriber(adapterPosition, followedStatus)
        )
    }

    private fun getFollowKolSubscriber(adapterPosition: Int, followStatus: Boolean) : Subscriber<GraphqlResponse> {
        return object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse) {
                followKolSubscriberOnNext(t.getData(FollowKolQuery::class.java), adapterPosition, followStatus)
            }

            override fun onCompleted() { }

            override fun onError(e: Throwable?) {
                followKolSubscriberOnError(e)
            }
        }
    }

    private fun followKolSubscriberOnNext(followResponseModel : FollowKolQuery?, adapterPosition: Int, followStatus: Boolean) {
        if(followResponseModel == null) {
            view?.onErrorToggleFollow()
            return
        }

        if (followResponseModel.data.error != null) {
            view?.onSuccessToggleFollow(adapterPosition, (!followStatus))
        } else {
            view?.onErrorToggleFollow(followResponseModel.data.error ?: "")
        }
    }

    private fun followKolSubscriberOnError(e: Throwable?) {
        e?.printStackTrace()
        view?.onErrorToggleFollow()
    }

    override fun requestProfileListData(query: String, page: Int) {
        nextPage = page

        val startRow : Int = (page - 1) * SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE

        searchProfileListUseCase.execute(
            createSearchProfileRequestParams(query, startRow),
            getSearchProfileSubscriber(startRow + 1)
        )
    }

    private fun createSearchProfileRequestParams(query: String, startRow: Int): RequestParams {
        val requestParams = RequestParams.create()

        requestParams.putString(SearchApiConst.Q, query)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_MOBILE)
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putString(SearchApiConst.ROWS, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE.toString())

        requestParams.putString(SearchApiConst.START, startRow.toString())
        return requestParams
    }

    private fun getSearchProfileSubscriber(startRow: Int): Subscriber<SearchProfileModel> {
        return object : Subscriber<SearchProfileModel>() {
            override fun onNext(searchProfileModel: SearchProfileModel?) {
                searchProfileOnNext(searchProfileModel, startRow)
            }

            override fun onCompleted() { }

            override fun onError(e: Throwable?) {
                searchProfileOnError(e)
            }
        }
    }

    private fun searchProfileOnNext(searchProfileModel: SearchProfileModel?, startRow: Int) {
        if(searchProfileModel == null) {
            view?.onErrorGetProfileListData()
            return
        }

        handleSearchProfileSuccess(searchProfileModel, startRow)
    }

    private fun handleSearchProfileSuccess(searchProfileModel: SearchProfileModel, startRow: Int) {
        val profileListViewModel = profileListViewModelMapper.convert(searchProfileModel)

        hasNextPage = profileListViewModel.isHasNextPage
        totalProfileCount = profileListViewModel.totalSearchCount

        if (hasSearchResult()) {
            handleSearchProfileResult(profileListViewModel, startRow)
        }
        else {
            handleSearchProfileEmptyResult(profileListViewModel)
        }
    }

    private fun hasSearchResult(): Boolean {
        return totalProfileCount > 0
    }

    private fun handleSearchProfileResult(profileListViewModel: ProfileListViewModel, startRow: Int) {
        profileViewModelListIncrementPosition(profileListViewModel.profileModelList, startRow)

        view?.onSuccessGetProfileListData(profileListViewModel)
    }

    private fun handleSearchProfileEmptyResult(profileListViewModel: ProfileListViewModel) {
        val hasRecommendationProfile = profileListViewModel.recommendationProfileModelList.isNotEmpty()
        val emptyResultVisitableList = createEmptyResultVisitableList(hasRecommendationProfile, profileListViewModel)

        getViewToTrackEmptyResultEvents(hasRecommendationProfile, profileListViewModel)
        getViewToPrepareRenderingList()
        getViewToRenderVisitableList(emptyResultVisitableList)
    }

    private fun createEmptyResultVisitableList(
            hasRecommendationProfile: Boolean,
            profileListViewModel: ProfileListViewModel
    ): List<Visitable<*>> {
        val emptyResultVisitableList = mutableListOf<Visitable<*>>()

        emptyResultVisitableList.addEmptySearchProfileViewModel()

        if (hasRecommendationProfile) {
            emptyResultVisitableList.addRecommendationProfiles(profileListViewModel)
        }

        return emptyResultVisitableList
    }

    private fun MutableList<Visitable<*>>.addEmptySearchProfileViewModel() {
        this.add(EmptySearchProfileViewModel())
    }

    private fun MutableList<Visitable<*>>.addRecommendationProfiles(profileListViewModel: ProfileListViewModel) {
        this.add(ProfileRecommendationTitleViewModel())

        profileViewModelListIncrementPosition(profileListViewModel.recommendationProfileModelList, 1)
        this.addAll(profileListViewModel.recommendationProfileModelList)
    }

    private fun getViewToTrackEmptyResultEvents(hasRecommendationProfile: Boolean, profileListViewModel: ProfileListViewModel) {
        view?.trackEmptySearchProfile()

        if (hasRecommendationProfile) {
            view?.trackImpressionRecommendationProfile(profileListViewModel.getRecommendationListTrackingObject())
        }
    }

    private fun getViewToPrepareRenderingList() {
        view?.hideLoading()
        view?.clearVisitableList()
    }

    private fun getViewToRenderVisitableList(visitableList: List<Visitable<*>>) {
        view?.renderVisitableList(visitableList)
    }

    private fun profileViewModelListIncrementPosition(profileViewModelList : List<ProfileViewModel>, startRow : Int) {
        var position = startRow

        for(item in profileViewModelList) {
            item.position = position++
        }
    }

    private fun searchProfileOnError(e: Throwable?) {
        e?.printStackTrace()
        view?.onErrorGetProfileListData()
    }

    override fun onViewClickProfile(profileViewModel: ProfileViewModel) {
        getViewToTrackClickProfile(profileViewModel)
        getViewToRoutePage(ApplinkConst.PROFILE.replace(PARAM_USER_ID, profileViewModel.id))
    }

    private fun getViewToTrackClickProfile(profileViewModel: ProfileViewModel) {
        if (profileViewModel.isRecommendation) {
            view?.trackClickRecommendationProfile(profileViewModel)
        }
        else {
            view?.trackClickProfile(profileViewModel)
        }
    }

    private fun getViewToRoutePage(applink: String) {
        view?.route(applink)
    }
}