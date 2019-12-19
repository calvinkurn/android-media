package com.tokopedia.tokopedia.feedplus.view

import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase.Companion.createRequestParams
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interest_pick_common.data.DataItem
import com.tokopedia.interest_pick_common.data.OnboardingData
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel
import com.tokopedia.vote.domain.usecase.SendVoteUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

/**
 * @author by yoasfs on 2019-12-17
 */
fun TestBody.createFeedViewModel(): FeedViewModel{
    val userSession by memoized<UserSessionInterface>()
    val getInterestPickUseCase by memoized<GetInterestPickUseCase>()
    val submitInterestPickUseCase by memoized<SubmitInterestPickUseCase>()
    val getDynamicFeedFirstPageUseCase by memoized<GetDynamicFeedFirstPageUseCase>()
    val getDynamicFeedUseCase by memoized<GetDynamicFeedUseCase>()
    val doFavoriteShopUseCase by memoized<ToggleFavouriteShopUseCase>()
    val followKolPostGqlUseCase by memoized<FollowKolPostGqlUseCase>()
    val likeKolPostUseCase by memoized<LikeKolPostUseCase>()
    val sendVoteUseCase by memoized<SendVoteUseCase>()
    val atcUseCase by memoized<AddToCartUseCase>()
    val trackAffiliateClickUseCase by memoized<TrackAffiliateClickUseCase>()
    val deletePostUseCase by memoized<DeletePostUseCase>()

    return FeedViewModel(
            FeedTestDispatcherProvider(),
            userSession,
            getInterestPickUseCase,
            submitInterestPickUseCase,
            getDynamicFeedFirstPageUseCase,
            getDynamicFeedUseCase,
            doFavoriteShopUseCase,
            followKolPostGqlUseCase,
            likeKolPostUseCase,
            sendVoteUseCase,
            atcUseCase,
            trackAffiliateClickUseCase,
            deletePostUseCase
    )
}


@Suppress("UNUSED_VARIABLE")
fun FeatureBody.createFeedTestInstance() {
    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val getInterestPickUseCase by memoized {
        mockk<GetInterestPickUseCase>(relaxed = true)
    }

    val atcUseCase by memoized {
        mockk<AddToCartUseCase>(relaxed = true)
    }

    val submitInterestPickUseCase by memoized {
        mockk<SubmitInterestPickUseCase>(relaxed = true)
    }

    val getDynamicFeedFirstPageUseCase by memoized {
        mockk<GetDynamicFeedFirstPageUseCase>(relaxed = true)
    }

    val getDynamicFeedUseCase by memoized {
        mockk<GetDynamicFeedUseCase>(relaxed = true)
    }

    val doFavoriteShopUseCase by memoized {
        mockk<ToggleFavouriteShopUseCase>(relaxed = true)
    }

    val followKolPostGqlUseCase by memoized {
        mockk<FollowKolPostGqlUseCase>(relaxed = true)
    }

    val likeKolPostUseCase by memoized {
        mockk<LikeKolPostUseCase>(relaxed = true)
    }

    val sendVoteUseCase by memoized {
        mockk<SendVoteUseCase>(relaxed = true)
    }

    val trackAffiliateClickUseCase by memoized {
        mockk<TrackAffiliateClickUseCase>(relaxed = true)
    }

    val deletePostUseCase by memoized {
        mockk<DeletePostUseCase>(relaxed = true)
    }
}

fun GetDynamicFeedFirstPageUseCase.getFeedFirstDataWithSample(data: MutableList<Visitable<*>>, cursor: String = "") {
    coEvery {
        createObservable(any())
                .toBlocking().single()
    } returns
            DynamicFeedFirstPageDomainModel(DynamicFeedDomainModel(postList = data, cursor = cursor), false)
}

//fun GetDynamicFeedFirstPageUseCase.getErrorFeedData() {
//    coEvery {
//        createObservable(any())
//                .toBlocking().single()
//    } returns Throwable()
//}


fun GetDynamicFeedUseCase.getFeedNextDataWithSample(data: MutableList<Visitable<*>>) {
    coEvery {
        createObservable(any())
                .toBlocking().single()
    } returns
            DynamicFeedDomainModel(postList = data)
}
//
//fun GetInterestPickUseCase.getInterestPickDataWithSample(pojodata: MutableList<DataItem> ) {
//    coEvery {
//        execute(any(), any())
//    } answers {
//        val successResult = (firstArg() as (OnboardingData))
//        successResult.feedUserOnboardingInterests.data = pojodata
//    }
//}

fun GetInterestPickUseCase.getInterestPickDataWithSample(pojodata: MutableList<DataItem>, resultObserver: Observer<Result<OnboardingViewModel>>) {
    coEvery {
        execute({
            it.feedUserOnboardingInterests.data = pojodata
        },any())
    } answers {

    }
}

fun ToggleFavouriteShopUseCase.doFavoriteShopWithSample(isSuccess: Boolean) {
    coEvery {
        createObservable(any()).toBlocking().single()
    } returns isSuccess
}

fun LikeKolPostUseCase.doLikeKolWithSample(isSuccess: Boolean) {
    coEvery {
        createObservable(any()).toBlocking().first()
    } returns isSuccess
}

fun SendVoteUseCase.doVoteWithSample(model: VoteStatisticDomainModel) {
    coEvery {
        createObservable(any()).toBlocking().single()
    } returns model
}
