package com.tokopedia.feedplus.view.presenter

import android.content.Context
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.subscriber.TrackPostClickSubscriber
import com.tokopedia.feedplus.*
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase
import com.tokopedia.feedplus.view.listener.FeedPlus
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel
import com.tokopedia.vote.domain.usecase.SendVoteUseCase

import javax.inject.Inject

import rx.Subscriber

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusPresenter @Inject
internal constructor(@ApplicationContext private val context: Context,
                     private val userSession: UserSessionInterface,
                     private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
                     private val likeKolPostUseCase: LikeKolPostUseCase,
                     private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
                     private val sendVoteUseCase: SendVoteUseCase,
                     private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
                     private val atcUseCase: AddToCartUseCase) : BaseDaggerPresenter<FeedPlus.View>(), FeedPlus.Presenter {
    private var currentCursor = ""
    private lateinit var viewListener: FeedPlus.View

    override fun attachView(view: FeedPlus.View) {
        super.attachView(view)
        this.viewListener = view
    }

    override fun detachView() {
        super.detachView()
        doFavoriteShopUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        sendVoteUseCase.unsubscribe()
    }

}