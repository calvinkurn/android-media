package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.model.FollowResponseModel
import com.tokopedia.search.di.module.FollowKolPostUseCaseModule
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.domain.usecase.searchprofile.SearchProfileUseCaseModule
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.presenter.subscriber.FollowUnfollowKolSubscriber
import com.tokopedia.search.result.presentation.presenter.subscriber.SearchProfileSubscriber
import com.tokopedia.search.result.presentation.view.listener.FollowActionListener
import com.tokopedia.search.result.presentation.view.listener.SearchProfileListener
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named

private class ProfileListPresenter : BaseDaggerPresenter<ProfileListSectionContract.View>() , ProfileListSectionContract.Presenter {

    @field:[Inject Named(SearchConstant.SearchProfile.SEARCH_PROFILE_USE_CASE)]
    lateinit var searchProfileListUseCase : UseCase<SearchProfileModel>

    @Inject
    lateinit var followKolPostGqlUseCase : UseCase<FollowResponseModel>

    lateinit var followActionListener: FollowActionListener
    lateinit var searchProfileListener: SearchProfileListener

    override fun initInjector() {
        val component = DaggerProfileListPresenterComponent.builder()
            .baseAppComponent(view.getBaseAppComponent())
            .build()

        component.inject(this)
    }

    override fun attachSearchProfileListener(searchProfileListener: SearchProfileListener) {
        this.searchProfileListener = searchProfileListener
    }

    override fun attachFollowActionListener(followActionListener: FollowActionListener) {
        this.followActionListener = followActionListener
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
            FollowKolPostGqlUseCase.createRequestParams(
                userToFollowId,
                requestedAction
            ), FollowUnfollowKolSubscriber(adapterPosition, followedStatus, followActionListener)
        )
    }

    override fun requestProfileListData(query: String, page: Int) {
        val startRow : Int = (page - 1) * SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE

        searchProfileListUseCase.execute(
            createSearchProfileRequestParams(query, startRow),
            SearchProfileSubscriber(searchProfileListener, startRow + 1)
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
}

@SearchScope
@Component(modules = [
    SearchProfileUseCaseModule::class,
    FollowKolPostUseCaseModule::class
], dependencies = [
    BaseAppComponent::class
])
private interface ProfileListPresenterComponent {

    fun inject(profileListPresenter: ProfileListPresenter)
}

@SearchScope
@Module
class ProfileListPresenterModule {

    @SearchScope
    @Provides
    fun provideProfileListSectionPresenter() : ProfileListSectionContract.Presenter {
        return ProfileListPresenter()
    }
}