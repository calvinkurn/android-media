package com.tokopedia.tokopedia.feedplus.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.domain.usecase.*
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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
    doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
    followKolPostGqlUseCase: FollowKolPostGqlUseCase,
    likeKolPostUseCase: LikeKolPostUseCase,
    atcUseCase: AddToCartUseCase,
    trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
    deletePostUseCase: DeletePostUseCase,
    sendTopAdsUseCase: SendTopAdsUseCase,
    playWidgetTools: PlayWidgetTools,
    getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase,
    getWhitelistNewUseCase: GetWhitelistNewUseCase,
    sendReportUseCase: SendReportUseCase,
    addWishListUseCase: AddWishListUseCase,
    addToWishlistV2UseCase: AddToWishlistV2UseCase,
    trackVisitChannelBroadcasterUseCase: FeedBroadcastTrackerUseCase,
    feedXTrackViewerUseCase: FeedXTrackViewerUseCase
) : Closeable {

    val vm = FeedViewModel(
        dispatcher,
        userSession,
        doFavoriteShopUseCase,
        followKolPostGqlUseCase,
        likeKolPostUseCase,
        atcUseCase,
        trackAffiliateClickUseCase,
        deletePostUseCase,
        sendTopAdsUseCase,
        playWidgetTools,
        getDynamicFeedNewUseCase,
        getWhitelistNewUseCase,
        sendReportUseCase,
        addWishListUseCase,
        addToWishlistV2UseCase,
        trackVisitChannelBroadcasterUseCase,
        feedXTrackViewerUseCase
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

    fun create(
        dispatcher: CoroutineTestDispatchers,
        userSession: UserSessionInterface = mockk(relaxed = true),
        doFavoriteShopUseCase: ToggleFavouriteShopUseCase = mockk(relaxed = true),
        followKolPostGqlUseCase: FollowKolPostGqlUseCase = mockk(relaxed = true),
        likeKolPostUseCase: LikeKolPostUseCase = mockk(relaxed = true),
        atcUseCase: AddToCartUseCase = mockk(relaxed = true),
        trackAffiliateClickUseCase: TrackAffiliateClickUseCase = mockk(relaxed = true),
        deletePostUseCase: DeletePostUseCase = mockk(relaxed = true),
        sendTopAdsUseCase: SendTopAdsUseCase = mockk(relaxed = true),
        playWidgetTools: PlayWidgetTools = mockk(relaxed = true),
        getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase = mockk(relaxed = true),
        getWhitelistNewUseCase: GetWhitelistNewUseCase = mockk(relaxed = true),
        sendReportUseCase: SendReportUseCase = mockk(relaxed = true),
        addWishListUseCase: AddWishListUseCase = mockk(relaxed = true),
        addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true),
        trackVisitChannelBroadcasterUseCase: FeedBroadcastTrackerUseCase = mockk(relaxed = true),
        feedXTrackViewerUseCase: FeedXTrackViewerUseCase = mockk(relaxed = true)
    ) : FeedViewModelRobot{
        return FeedViewModelRobot(
            dispatcher = dispatcher,
            userSession = userSession,
            doFavoriteShopUseCase = doFavoriteShopUseCase,
            followKolPostGqlUseCase = followKolPostGqlUseCase,
            likeKolPostUseCase = likeKolPostUseCase,
            atcUseCase = atcUseCase,
            trackAffiliateClickUseCase = trackAffiliateClickUseCase,
            deletePostUseCase = deletePostUseCase,
            sendTopAdsUseCase = sendTopAdsUseCase,
            playWidgetTools = playWidgetTools,
            getDynamicFeedNewUseCase = getDynamicFeedNewUseCase,
            getWhitelistNewUseCase = getWhitelistNewUseCase,
            sendReportUseCase = sendReportUseCase,
            addToWishlistV2UseCase = addToWishlistV2UseCase,
            addWishListUseCase = addWishListUseCase,
            trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
            feedXTrackViewerUseCase = feedXTrackViewerUseCase
        )
    }
}
