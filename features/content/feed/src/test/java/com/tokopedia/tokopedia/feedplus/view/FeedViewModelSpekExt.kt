package com.tokopedia.tokopedia.feedplus.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.SendReportUseCase
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import com.tokopedia.interest_pick_common.domain.usecase.SubmitInterestPickUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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
    val doFavoriteShopUseCase by memoized<ToggleFavouriteShopUseCase>()
    val followKolPostGqlUseCase by memoized<FollowKolPostGqlUseCase>()
    val likeKolPostUseCase by memoized<LikeKolPostUseCase>()
    val atcUseCase by memoized<AddToCartUseCase>()
    val trackAffiliateClickUseCase by memoized<TrackAffiliateClickUseCase>()
    val deletePostUseCase by memoized<DeletePostUseCase>()
    val sendTopAdsUseCase by memoized<SendTopAdsUseCase>()
    val playWidgetTools by memoized<PlayWidgetTools>()
    val getDynamicFeedNewUseCase by memoized<GetDynamicFeedNewUseCase>()
    val getWhitelistNewUseCase by memoized<GetWhitelistNewUseCase>()
    val addWishListUseCase by memoized<AddWishListUseCase>()
    val sendReportUseCase by memoized<SendReportUseCase>()

    return FeedViewModel(
        CoroutineTestDispatchersProvider,
        userSession,
        getInterestPickUseCase,
        submitInterestPickUseCase,
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
        addWishListUseCase
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
        mockk<GetWhitelistNewUseCase>(relaxed = true)
    }
    val getDynamicFeedNewUseCase by memoized {
        mockk<GetDynamicFeedNewUseCase>(relaxed = true)
    }
    val addWishListUseCase by memoized {
        mockk<AddWishListUseCase>(relaxed = true)
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

fun GetWhitelistNewUseCase.getMockData(data: WhitelistQuery) {
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
