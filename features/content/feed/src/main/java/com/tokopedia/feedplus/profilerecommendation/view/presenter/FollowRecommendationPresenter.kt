package com.tokopedia.feedplus.profilerecommendation.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel
import com.tokopedia.feedplus.profilerecommendation.domain.model.FollowRecommendationQuery
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.GetFollowRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecommendationContract
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationPresenter @Inject constructor(
        private val getFollowRecommendationUseCase: GetFollowRecommendationUseCase
) : BaseDaggerPresenter<FollowRecommendationContract.View>(), FollowRecommendationContract.Presenter {

    override fun getFollowRecommendationList(idList: List<Int>, cursor: String) {
        getFollowRecommendationUseCase.apply {
            setRequestParams(GetFollowRecommendationUseCase.getRequestParams(idList, cursor))
            execute (onSuccess = {
                view.onGetFollowRecommendationList(getRecommendationCardList(it.feedUserOnboardingRecommendations), it.feedUserOnboardingRecommendations.meta.nextCursor)
                view.onGetFollowRecommendationInfo(getRecommendationInfo(it.feedUserOnboardingRecommendations))
            }, onError = {

            })
        }
    }

    private fun getRecommendationCardList(query: FollowRecommendationQuery): List<RecommendationCardViewModel> = query.data.map { data ->
        RecommendationCardViewModel(
                image1Url = if (data.media.isNotEmpty()) data.media[0].thumbnail else "",
                image2Url = if (data.media.size > 1) data.media[1].thumbnail else "",
                image3Url = if (data.media.size > 2) data.media[2].thumbnail else "",
                profileImageUrl = data.header.avatar,
                badgeUrl = data.header.avatarBadgeImage,
                profileName = data.header.avatarTitle,
                description = data.header.avatarDescription,
                redirectUrl = data.header.avatarApplink,
                cta = FollowCta(
                        textFalse = data.header.followCta.textFalse,
                        textTrue = data.header.followCta.textTrue
                )
        )
    }

    private fun getRecommendationInfo(query: FollowRecommendationQuery): FollowRecommendationInfoViewModel = FollowRecommendationInfoViewModel(
            instructionText = try { String.format(query.meta.assets.instruction, query.meta.minFollowed) } catch (e: Exception) { "" },
            buttonCTA = query.meta.assets.buttonCta
    )
}