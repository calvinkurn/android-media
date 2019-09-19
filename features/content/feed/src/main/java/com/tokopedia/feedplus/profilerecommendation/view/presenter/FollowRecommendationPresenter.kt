package com.tokopedia.feedplus.profilerecommendation.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.feedplus.profilerecommendation.data.AuthorType
import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowRecommendationQuery
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.FollowAllRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.GetFollowRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.SetOnboardingStatusUseCase
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecommendationContract
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecommendationAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.model.FollowResponseModel
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationPresenter @Inject constructor(
        private val getFollowRecommendationUseCase: GetFollowRecommendationUseCase,
        private val followAllRecommendationUseCase: FollowAllRecommendationUseCase,
        private val setOnboardingStatusUseCase: SetOnboardingStatusUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase
) : BaseDaggerPresenter<FollowRecommendationContract.View>(), FollowRecommendationContract.Presenter {

    override fun getFollowRecommendationList(interestIds: IntArray, cursor: String) {
        if (cursor == "") view.showLoading()
        getFollowRecommendationUseCase.apply {
            setRequestParams(GetFollowRecommendationUseCase.getRequestParams(interestIds, cursor))
            execute (onSuccess = {
                view.onGetFollowRecommendationList(getRecommendationCardList(it.feedUserOnboardingRecommendations), it.feedUserOnboardingRecommendations.meta.nextCursor)
                view.onGetFollowRecommendationInfo(getRecommendationInfo(it.feedUserOnboardingRecommendations))
                view.hideLoading()
            }, onError = {
                view.onGetError(it)
                view.hideLoading()
            })
        }
    }

    override fun followAllRecommendation(interestIds: IntArray) {
        view.showLoading()
        followAllRecommendationUseCase.apply {
            setRequestParams(FollowAllRecommendationUseCase.getRequestParams(interestIds))
            execute(onSuccess = {
                view.onSuccessFollowAllRecommendation()
                view.hideLoading()
            }, onError = {
                view.onGetError(it)
                view.hideLoading()
            })
        }
    }

    override fun followUnfollowRecommendation(id: String, action: FollowRecommendationAction) {
        followKolPostGqlUseCase.execute(
                FollowKolPostGqlUseCase.createRequestParams(id.toInt(),
                        when (action) {
                            FollowRecommendationAction.FOLLOW -> FollowKolPostGqlUseCase.PARAM_FOLLOW
                            FollowRecommendationAction.UNFOLLOW -> FollowKolPostGqlUseCase.PARAM_UNFOLLOW
                        }),
                object : Subscriber<FollowResponseModel>() {
                    override fun onNext(t: FollowResponseModel) {
                        if (t.errorMessage.isNullOrEmpty() && t.isSuccess) {
                            when (action) {
                                FollowRecommendationAction.FOLLOW -> view.onSuccessFollowRecommendation(id)
                                FollowRecommendationAction.UNFOLLOW -> view.onSuccessUnfollowRecommendation(id)
                            }
                        }
                        else view.onGetError(IllegalStateException(t.errorMessage))
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.let(view::onGetError)
                    }
                }
        )
    }

    override fun setOnboardingStatus() {
        view.showLoading()
        setOnboardingStatusUseCase.apply {
            execute(onSuccess = {
                view.onFinishSetOnboardingStatus()
                view.hideLoading()
            }, onError = {
                view.onFinishSetOnboardingStatus()
                view.hideLoading()
            })
        }
    }

    private fun getRecommendationCardList(query: FollowRecommendationQuery): List<FollowRecommendationCardViewModel> = query.data.map { data ->
        FollowRecommendationCardViewModel(
                header = query.meta.assets.title,
                image1Url = if (data.media.isNotEmpty()) data.media[0].thumbnail else "",
                image2Url = if (data.media.size > 1) data.media[1].thumbnail else "",
                image3Url = if (data.media.size > 2) data.media[2].thumbnail else "",
                avatar = data.header.avatar,
                badgeUrl = data.header.avatarBadgeImage,
                title = data.header.avatarTitle,
                description = data.header.avatarDescription,
                enabledFollowText = data.header.followCta.textFalse,
                disabledFollowText = data.header.followCta.textTrue,
                isFollowed = data.header.followCta.isFollow,
                followInstruction = if (AuthorType.findTypeByString(data.header.followCta.authorType) == AuthorType.SHOP) query.meta.assets.shopDescription else query.meta.assets.profileDescription,
                authorId = data.header.followCta.authorID
        )
    }

    private fun getRecommendationInfo(query: FollowRecommendationQuery): FollowRecommendationInfoViewModel = FollowRecommendationInfoViewModel(
            minFollowed = query.meta.minFollowed,
            instructionText = query.meta.assets.instruction,
            buttonCTA = query.meta.assets.buttonCta
    )

    override fun detachView() {
        super.detachView()
        followKolPostGqlUseCase.unsubscribe()
        followAllRecommendationUseCase.cancelJobs()
        getFollowRecommendationUseCase.cancelJobs()
    }
}