package com.tokopedia.feedplus.profilerecommendation.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.feedplus.profilerecommendation.data.AuthorType
import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowRecommendationMedia
import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowRecommendationQuery
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.FollowAllRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.GetFollowRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.SetOnboardingStatusUseCase
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecomContract
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecomAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardThumbnailViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomInfoViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.model.FollowResponseModel
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecomPresenter @Inject constructor(
        private val getFollowRecommendationUseCase: GetFollowRecommendationUseCase,
        private val followAllRecommendationUseCase: FollowAllRecommendationUseCase,
        private val setOnboardingStatusUseCase: SetOnboardingStatusUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase
) : BaseDaggerPresenter<FollowRecomContract.View>(), FollowRecomContract.Presenter {

    override fun getFollowRecommendationList(interestIds: IntArray, cursor: String) {
        if (cursor == "") view.showListLoading()
        getFollowRecommendationUseCase.apply {
            setRequestParams(GetFollowRecommendationUseCase.getRequestParams(interestIds, cursor))
            execute (onSuccess = {
                view.onGetFollowRecommendationList(getRecommendationCardList(it.feedUserOnboardingRecommendations), it.feedUserOnboardingRecommendations.meta.nextCursor)
                view.onGetFollowRecommendationInfo(getRecommendationInfo(it.feedUserOnboardingRecommendations))
                view.hideListLoading()
            }, onError = {
                view.onGetError(it)
                view.hideListLoading()
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

    override fun followUnfollowRecommendation(id: String, action: FollowRecomAction) {
        followKolPostGqlUseCase.execute(
                FollowKolPostGqlUseCase.createRequestParams(id.toInt(),
                        when (action) {
                            FollowRecomAction.FOLLOW -> FollowKolPostGqlUseCase.PARAM_FOLLOW
                            FollowRecomAction.UNFOLLOW -> FollowKolPostGqlUseCase.PARAM_UNFOLLOW
                        }),
                object : Subscriber<FollowResponseModel>() {
                    override fun onNext(t: FollowResponseModel) {
                        if (t.errorMessage.isNullOrEmpty() && t.isSuccess) {
                            view.onSuccessFollowUnfollowRecommendation(id, action)
                        }
                        else {
                            view.onFailedFollowUnfollowRecommendation(
                                    id,
                                    action,
                                    IllegalStateException(t.errorMessage)
                            )

                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.let {
                            view.onFailedFollowUnfollowRecommendation(
                                    id,
                                    action,
                                    it
                            )
                        }
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

    override fun detachView() {
        super.detachView()
        followKolPostGqlUseCase.unsubscribe()
        followAllRecommendationUseCase.cancelJobs()
        getFollowRecommendationUseCase.cancelJobs()
    }

    private fun getRecommendationCardList(query: FollowRecommendationQuery): List<FollowRecomCardViewModel> = query.data.map { data ->
        FollowRecomCardViewModel(
                header = query.meta.assets.title,
                thumbnailList = data.media.map(::getRecommendationCardThumbnail),
                avatar = data.header.avatar,
                badgeUrl = data.header.avatarBadgeImage,
                title = data.header.avatarTitle,
                description = data.header.avatarDescription,
                enabledFollowText = data.header.followCta.textFalse,
                disabledFollowText = data.header.followCta.textTrue,
                isFollowed = data.header.followCta.isFollow,
                textFollowTrue = data.header.followCta.textTrue,
                textFollowFalse = data.header.followCta.textFalse,
                followInstruction = if (AuthorType.findTypeByString(data.header.followCta.authorType) == AuthorType.SHOP) query.meta.assets.shopDescription else query.meta.assets.profileDescription,
                authorId = data.header.followCta.authorID,
                authorType = AuthorType.findTypeByString(data.header.followCta.authorType)
        )
    }

    private fun getRecommendationCardThumbnail(media: FollowRecommendationMedia): FollowRecomCardThumbnailViewModel = FollowRecomCardThumbnailViewModel(
            id = media.id,
            url = media.thumbnail
    )

    private fun getRecommendationInfo(query: FollowRecommendationQuery): FollowRecomInfoViewModel = FollowRecomInfoViewModel(
            minFollowed = query.meta.minFollowed,
            instructionText = query.meta.assets.instruction,
            buttonCTA = query.meta.assets.buttonCta
    )
}