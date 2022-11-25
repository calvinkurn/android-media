package com.tokopedia.tokopedia.feedplus.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.*
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

/**
 * @author by yoasfs on 2019-12-17
 */
fun TestBody.createFeedViewModel(): FeedViewModel{
    val userSession by memoized<UserSessionInterface>()
    val likeKolPostUseCase by memoized<LikeKolPostUseCase>()
    val atcUseCase by memoized<com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase>()
    val trackAffiliateClickUseCase by memoized<TrackAffiliateClickUseCase>()
    val deletePostUseCase by memoized<DeletePostUseCase>()
    val sendTopAdsUseCase by memoized<SendTopAdsUseCase>()
    val playWidgetTools by memoized<PlayWidgetTools>()
    val getDynamicFeedNewUseCase by memoized<GetDynamicFeedNewUseCase>()
    val getWhitelistNewUseCase by memoized<GetWhiteListNewUseCase>()
    val addToWishlistV2UseCase by memoized<AddToWishlistV2UseCase>()
    val sendReportUseCase by memoized<SendReportUseCase>()
    val feedBroadcastTrackerUseCase by memoized<FeedBroadcastTrackerUseCase>()
    val feedXTrackViewerUseCase by memoized<FeedXTrackViewerUseCase>()
    val feedXCheckUpcomingCapaignReminderUseCase by memoized<CheckUpcomingCampaignReminderUseCase>()
    val postUpcomingCampaignReminderUseCase by memoized<PostUpcomingCampaignReminderUseCase>()
    val shopRecomUseCase by memoized<ShopRecomUseCase>()
    val shopRecomMapper by memoized<ShopRecomUiMapper>()
    val shopFollowUseCase by memoized<ShopFollowUseCase>()
    val doFollowUseCase by memoized<ProfileUnfollowedUseCase>()
    val doUnfollowUseCase by memoized<ProfileFollowUseCase>()
    val profileMutationMapper by memoized<ProfileMutationMapper>()

    return FeedViewModel(
        baseDispatcher = CoroutineTestDispatchersProvider,
        userSession = userSession,
        likeKolPostUseCase = likeKolPostUseCase,
        addToCartUseCase = atcUseCase,
        trackAffiliateClickUseCase = trackAffiliateClickUseCase,
        deletePostUseCase = deletePostUseCase,
        sendReportUseCase = sendReportUseCase,
        playWidgetTools = playWidgetTools,
        trackVisitChannelBroadcasterUseCase = feedBroadcastTrackerUseCase,
        getDynamicFeedNewUseCase = getDynamicFeedNewUseCase,
        getWhiteListNewUseCase = getWhitelistNewUseCase,
        addToWishlistV2UseCase = addToWishlistV2UseCase,
        feedXTrackViewerUseCase = feedXTrackViewerUseCase,
        sendTopAdsUseCase = sendTopAdsUseCase,
        shopRecomUseCase = shopRecomUseCase,
        shopRecomMapper = shopRecomMapper,
        checkUpcomingCampaignReminderUseCase = feedXCheckUpcomingCapaignReminderUseCase,
        postUpcomingCampaignReminderUseCase = postUpcomingCampaignReminderUseCase,
        shopFollowUseCase = shopFollowUseCase,
        doUnfollowUseCase = doFollowUseCase,
        profileMutationMapper = profileMutationMapper,
        doFollowUseCase = doUnfollowUseCase
    )
}


@Suppress("UNUSED_VARIABLE")
fun FeatureBody.createFeedTestInstance() {
    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val atcUseCase by memoized {
        mockk<AddToCartUseCase>(relaxed = true)
    }

    val getDynamicFeedFirstPageUseCase by memoized {
        mockk<GetDynamicFeedFirstPageUseCase>(relaxed = true)
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

    val trackAffiliateClickUseCase by memoized {
        mockk<TrackAffiliateClickUseCase>(relaxed = true)
    }

    val deletePostUseCase by memoized {
        mockk<DeletePostUseCase>(relaxed = true)
    }

    val sendTopAdsUseCase by memoized {
        mockk<SendTopAdsUseCase>(relaxed = true)
    }

    val playWidgetTools by memoized {
        mockk<PlayWidgetTools>(relaxed = true)
    }
    val getWhitelistNewUseCase by memoized {
        mockk<GetWhiteListNewUseCase>(relaxed = true)
    }
    val getDynamicFeedNewUseCase by memoized {
        mockk<GetDynamicFeedNewUseCase>(relaxed = true)
    }
    val addWishListV2UseCase by memoized {
        mockk<AddToWishlistV2UseCase>(relaxed = true)
    }
    val sendReportUseCase by memoized {
        mockk<SendReportUseCase>(relaxed = true)
    }

}

fun GetDynamicFeedNewUseCase.getMockData(data: MutableList<Visitable<*>>, cursor: String = "") {
    coEvery {
        execute(cursor)
    } returns
            DynamicFeedDomainModel(postList = data)
}

fun GetWhiteListNewUseCase.getMockData(data: GetCheckWhitelistResponse) {
    coEvery {
        execute("interest")
    } returns data

}

fun LikeKolPostUseCase.doLikeKolWithSample(isSuccess: Boolean) {
    coEvery {
        createObservable(any()).toBlocking().first()
    } returns isSuccess
}

fun AddToCartUseCase.doAtcWithSample(success: Int) {
    coEvery{
        createObservable(any()).toBlocking().single()
    } returns AddToCartDataModel(data = DataModel(success = success))
}

fun ToggleFavouriteShopUseCase.doToggleFavoriteShopWithSample(isSuccess: Boolean) {
    coEvery {
        createObservable(any()).toBlocking().first()
    } returns isSuccess
}

fun TrackAffiliateClickUseCase.doTrackAffiliateWithSample(isSuccess: Boolean) {
    coEvery {
        createObservable(any()).toBlocking().first()
    } returns isSuccess
}
