package com.tokopedia.tokopedia.feedplus.robot

import androidx.lifecycle.viewModelScope
import com.nhaarman.mockitokotlin2.mock
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.usecase.GetWhiteListUseCase
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.domain.usecase.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetFollowingUseCase
import com.tokopedia.feedcomponent.domain.usecase.PostUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapperImpl
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapperImpl
import com.tokopedia.feedplus.oldFeed.view.presenter.FeedViewModel
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.cancelChildren
import java.io.Closeable

/**
 * @author by astidhiyaa on 23/09/22
 */
class FeedViewModelRobot(
    dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    likeKolPostUseCase: SubmitLikeContentUseCase,
    atcUseCase: AddToCartUseCase,
    trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
    deletePostUseCase: SubmitActionContentUseCase,
    sendTopAdsUseCase: SendTopAdsUseCase,
    playWidgetTools: PlayWidgetTools,
    getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase,
    getWhitelistNewUseCase: GetWhiteListUseCase,
    sendReportUseCase: SubmitReportContentUseCase,
    addToWishlistV2UseCase: AddToWishlistV2UseCase,
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    feedXTrackViewerUseCase: FeedXTrackViewerUseCase,
    shopRecomUseCase: ShopRecomUseCase,
    shopRecomMapper: ShopRecomUiMapper,
    checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase,
    postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase,
    shopFollowUseCase: ShopFollowUseCase,
    doFollowUseCase: ProfileFollowUseCase,
    doUnfollowUseCase: ProfileUnfollowedUseCase,
    profileMutationMapper: ProfileMutationMapper,
    getFollowingUseCase: GetFollowingUseCase
) : Closeable {

    val vm = FeedViewModel(
        baseDispatcher = dispatcher,
        userSession = userSession,
        likeKolPostUseCase = likeKolPostUseCase,
        trackAffiliateClickUseCase = trackAffiliateClickUseCase,
        deletePostUseCase = deletePostUseCase,
        addToCartUseCase = atcUseCase,
        sendTopAdsUseCase = sendTopAdsUseCase,
        playWidgetTools = playWidgetTools,
        shopRecomUseCase = shopRecomUseCase,
        shopRecomMapper = shopRecomMapper,
        getDynamicFeedNewUseCase = getDynamicFeedNewUseCase,
        sendReportUseCase = sendReportUseCase,
        getWhiteListUseCase = getWhitelistNewUseCase,
        addToWishlistV2UseCase = addToWishlistV2UseCase,
        trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
        feedXTrackViewerUseCase = feedXTrackViewerUseCase,
        checkUpcomingCampaignReminderUseCase = checkUpcomingCampaignReminderUseCase,
        postUpcomingCampaignReminderUseCase = postUpcomingCampaignReminderUseCase,
        shopFollowUseCase = shopFollowUseCase,
        doFollowUseCase = doFollowUseCase,
        doUnfollowUseCase = doUnfollowUseCase,
        profileMutationMapper = profileMutationMapper,
        getFollowingUseCase = getFollowingUseCase
    )

    fun setLoggedIn(isUserLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isUserLoggedIn
    }

    fun setUserId(userId: String) {
        every { userSession.userId } returns userId
    }

    override fun close() {
        vm.viewModelScope.coroutineContext.cancelChildren()
    }
}

fun create(
    dispatcher: CoroutineTestDispatchers,
    userSession: UserSessionInterface = mockk(relaxed = true),
    likeKolPostUseCase: SubmitLikeContentUseCase = mockk(relaxed = true),
    atcUseCase: AddToCartUseCase = mockk(relaxed = true),
    trackAffiliateClickUseCase: TrackAffiliateClickUseCase = mockk(relaxed = true),
    deletePostUseCase: SubmitActionContentUseCase = mockk(relaxed = true),
    sendTopAdsUseCase: SendTopAdsUseCase = mockk(relaxed = true),
    playWidgetTools: PlayWidgetTools = mockk(relaxed = true),
    getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase = mockk(relaxed = true),
    getWhitelistNewUseCase: GetWhiteListUseCase = mockk(relaxed = true),
    sendReportUseCase: SubmitReportContentUseCase = mockk(relaxed = true),
    addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true),
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
    feedXTrackViewerUseCase: FeedXTrackViewerUseCase = mockk(relaxed = true),
    shopRecomUseCase: ShopRecomUseCase = mockk(relaxed = true),
    shopRecomMapper: ShopRecomUiMapper = ShopRecomUiMapperImpl(),
    checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase = mockk(relaxed = true),
    postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase = mockk(relaxed = true),
    shopFollowUseCase: ShopFollowUseCase = mockk(relaxed = true),
    doFollowUseCase: ProfileFollowUseCase = mockk(relaxed = true),
    doUnfollowUseCase: ProfileUnfollowedUseCase = mockk(relaxed = true),
    profileMutationMapper: ProfileMutationMapper = ProfileMutationMapperImpl(mock()),
    getFollowingUseCase: GetFollowingUseCase = mockk(relaxed = true),
    fn: FeedViewModelRobot.() -> Unit = {}
): FeedViewModelRobot {
    return FeedViewModelRobot(
        dispatcher = dispatcher,
        userSession = userSession,
        likeKolPostUseCase = likeKolPostUseCase,
        trackAffiliateClickUseCase = trackAffiliateClickUseCase,
        deletePostUseCase = deletePostUseCase,
        atcUseCase = atcUseCase,
        sendTopAdsUseCase = sendTopAdsUseCase,
        playWidgetTools = playWidgetTools,
        shopRecomUseCase = shopRecomUseCase,
        shopRecomMapper = shopRecomMapper,
        getDynamicFeedNewUseCase = getDynamicFeedNewUseCase,
        sendReportUseCase = sendReportUseCase,
        addToWishlistV2UseCase = addToWishlistV2UseCase,
        trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
        feedXTrackViewerUseCase = feedXTrackViewerUseCase,
        checkUpcomingCampaignReminderUseCase = checkUpcomingCampaignReminderUseCase,
        postUpcomingCampaignReminderUseCase = postUpcomingCampaignReminderUseCase,
        shopFollowUseCase = shopFollowUseCase,
        doFollowUseCase = doFollowUseCase,
        doUnfollowUseCase = doUnfollowUseCase,
        profileMutationMapper = profileMutationMapper,
        getWhitelistNewUseCase = getWhitelistNewUseCase,
        getFollowingUseCase = getFollowingUseCase
    ).apply(fn)
}
