package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.model.FollowResponseModel
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.mapper.ProfileListViewModelMapper
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

// This class is currently made 'public' due to Kotlin not having 'private package' access modifier
// Please avoid using this class directly, and use ProfileListSectionContract.Presenter instead
class ProfileListPresenter : BaseDaggerPresenter<ProfileListSectionContract.View>() , ProfileListSectionContract.Presenter {

    @field:[Inject Named(SearchConstant.SearchProfile.SEARCH_PROFILE_USE_CASE)]
    lateinit var searchProfileListUseCase : UseCase<SearchProfileModel>

    @Inject
    lateinit var followKolPostGqlUseCase : UseCase<FollowResponseModel>

    @Inject
    lateinit var profileListViewModelMapper: Mapper<SearchProfileModel, ProfileListViewModel>

    override fun initInjector() {
        val component = DaggerProfileListPresenterComponent.builder()
            .baseAppComponent(view.getBaseAppComponent())
            .build()

        component.inject(this)
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
            FollowKolPostGqlUseCase.createRequestParams(userToFollowId, requestedAction),
            getFollowKolSubscriber(adapterPosition, followedStatus)
        )
    }

    private fun getFollowKolSubscriber(adapterPosition: Int, followStatus: Boolean) : Subscriber<FollowResponseModel> {
        return object : Subscriber<FollowResponseModel>() {
            override fun onNext(followResponseModel: FollowResponseModel?) {
                followKolSubscriberOnNext(followResponseModel, adapterPosition, followStatus)
            }

            override fun onCompleted() { }

            override fun onError(e: Throwable?) {
                followKolSubscriberOnError(e)
            }
        }
    }

    private fun followKolSubscriberOnNext(followResponseModel : FollowResponseModel?, adapterPosition: Int, followStatus: Boolean) {
        if(followResponseModel == null) {
            view.onErrorToggleFollow()
            return
        }

        if (followResponseModel.isSuccess) {
            view.onSuccessToggleFollow(adapterPosition, (!followStatus))
        } else {
            view.onErrorToggleFollow(followResponseModel.errorMessage ?: "")
        }
    }

    private fun followKolSubscriberOnError(e: Throwable?) {
        e?.printStackTrace()
        view.onErrorToggleFollow()
    }

    override fun requestProfileListData(query: String, page: Int) {
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
            view.onErrorGetProfileListData()
            return
        }

        val profileListViewModel = profileListViewModelMapper.convert(searchProfileModel)
        profileListViewModelIncrementPosition(profileListViewModel, startRow)

        view.onSuccessGetProfileListData(profileListViewModel)
    }

    private fun profileListViewModelIncrementPosition(profileListViewModel : ProfileListViewModel, startRow : Int) {
        var position = startRow

        for(item in profileListViewModel.profileModelList) {
            item.position = position++
        }
    }

    private fun searchProfileOnError(e: Throwable?) {
        e?.printStackTrace()
        view.onErrorGetProfileListData()
    }
}