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
                     private val getDynamicFeedFirstPageUseCase: GetDynamicFeedFirstPageUseCase,
                     private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
                     private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
                     private val atcUseCase: AddToCartUseCase,
                     private val deletePostUseCase: DeletePostUseCase) : BaseDaggerPresenter<FeedPlus.View>(), FeedPlus.Presenter {
    private var currentCursor = ""
    private lateinit var viewListener: FeedPlus.View
    private val pagingHandler: PagingHandler

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else NON_LOGIN_USER_ID

    init {
        this.pagingHandler = PagingHandler()
    }

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

    override fun fetchFirstPage(firstPageCursor: String) {
        getFirstPageFeed(firstPageCursor)
    }

    override fun fetchNextPage() {
        getNextPageFeed()
    }

    override fun favoriteShop(promotedShopViewModel: Data, adapterPosition: Int) {
        val PARAM_SHOP_DOMAIN = "shop_domain"
        val PARAM_SRC = "src"
        val PARAM_AD_KEY = "ad_key"
        val DEFAULT_VALUE_SRC = "fav_shop"
        val params = ToggleFavouriteShopUseCase.createRequestParam(promotedShopViewModel.shop.id)

        params.putString(PARAM_SHOP_DOMAIN, promotedShopViewModel.shop.domain)
        params.putString(PARAM_SRC, DEFAULT_VALUE_SRC)
        params.putString(PARAM_AD_KEY, promotedShopViewModel.adRefKey)

        doFavoriteShopUseCase.execute(params, object : Subscriber<Boolean>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(isSuccess: Boolean?) {
                val stringBuilder = StringBuilder()

                if (isSuccess!!) {
                    stringBuilder.append(MethodChecker.fromHtml(promotedShopViewModel.shop.name)).append(" ")
                    if (promotedShopViewModel.isFavorit) {
                        stringBuilder.append(viewListener.getString(R.string.shop_success_unfollow))
                    } else {
                        stringBuilder.append(viewListener.getString(R.string.shop_success_follow))
                    }
                } else {
                    stringBuilder.append(viewListener.getString(R.string.msg_network_error))
                }
                viewListener.showSnackbar(stringBuilder.toString())

                if (viewListener.hasFeed())
                    viewListener.updateFavorite(adapterPosition)
                else
                    viewListener.updateFavoriteFromEmpty(promotedShopViewModel.shop.id)
            }
        })
    }

    override fun setCursor(cursor: String) {
        this.currentCursor = cursor
    }

    override fun followKol(id: Int, rowNumber: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW))
        followKolPostGqlUseCase.execute(object: Subscriber<GraphqlResponse>() {
            override fun onNext(response: GraphqlResponse) {
                val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
                if (query.getData() != null) {
                    val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                    if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS)
                        view.onSuccessFollowUnfollowKol(rowNumber)
                    else {
                        view.onErrorFollowKol(context.getString(R.string
                                .default_request_error_unknown), id, FollowKolPostGqlUseCase.PARAM_FOLLOW, rowNumber)
                    }
                } else {
                    view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, Throwable()), id,
                            FollowKolPostGqlUseCase.PARAM_FOLLOW, rowNumber)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, e), id,
                        FollowKolPostGqlUseCase.PARAM_FOLLOW, rowNumber)
            }
        })
    }

    override fun unfollowKol(id: Int, rowNumber: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW))
        followKolPostGqlUseCase.execute(object: Subscriber<GraphqlResponse>() {
            override fun onNext(response: GraphqlResponse) {
                val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
                if (query.getData() != null) {
                    val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                    if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS)
                        view.onSuccessFollowUnfollowKol(rowNumber)
                    else {
                        view.onErrorFollowKol(context.getString(R.string
                                .default_request_error_unknown), id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber)
                    }
                } else {
                    view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, Throwable()), id,
                            FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, e), id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber)
            }
        })
    }

    override fun likeKol(id: Int, rowNumber: Int) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like),
                object : Subscriber<Boolean>() {
                    override fun onNext(success: Boolean) {
                        if (success) {
                            view.onSuccessLikeDislikeKolPost(rowNumber)
                        } else {
                            view.onErrorLikeDislikeKolPost(view.getString(R
                                    .string.default_request_error_unknown))
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        view.onErrorLikeDislikeKolPost(ErrorHandler.getErrorMessage(context, e))
                    }
                })

    }

    override fun unlikeKol(id: Int, rowNumber: Int) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike),
                object : Subscriber<Boolean>() {
                    override fun onNext(success: Boolean) {
                        if (success) {
                            view.onSuccessLikeDislikeKolPost(rowNumber)
                        } else {
                            view.onErrorLikeDislikeKolPost(view.getString(R
                                    .string.default_request_error_unknown))
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        view.onErrorLikeDislikeKolPost(ErrorHandler.getErrorMessage(context, e))
                    }
                })
    }

    override fun followKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        val status = FollowKolPostGqlUseCase.PARAM_FOLLOW
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, status))
        followKolPostGqlUseCase.execute(object: Subscriber<GraphqlResponse>() {
            override fun onNext(response: GraphqlResponse) {
                val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
                if (query.getData() != null) {
                    val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                    if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                        view.onSuccessFollowKolFromRecommendation(
                                rowNumber,
                                position,
                                status == FollowKolPostGqlUseCase.PARAM_FOLLOW
                        )
                    } else {
                        view.onErrorFollowKol(context.getString(R.string
                                .failed_to_follow), id, status, rowNumber)
                    }
                } else {
                    view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, Throwable()), id,
                            status, rowNumber)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, e), id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber)
            }
        })

    }

    override fun unfollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        val status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, status))
        followKolPostGqlUseCase.execute(object: Subscriber<GraphqlResponse>() {
            override fun onNext(response: GraphqlResponse) {
                val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
                if (query.getData() != null) {
                    val followKolDomain = FollowKolDomain(query.getData().getData().getStatus())
                    if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                        view.onSuccessFollowKolFromRecommendation(
                                rowNumber,
                                position,
                                status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW
                        )
                    } else {
                        view.onErrorFollowKol(context.getString(R.string
                                .failed_to_unfollow), id, status, rowNumber)
                    }
                } else {
                    view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, Throwable()), id,
                            status, rowNumber)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.onErrorFollowKol(ErrorHandler.getErrorMessage(context, e), id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber)
            }
        })

    }

    override fun sendVote(rowNumber: Int, pollId: String, optionId: String) {
        sendVoteUseCase.execute(
                SendVoteUseCase.createParamsV1(pollId, optionId),
                object: Subscriber<VoteStatisticDomainModel>() {
                    override fun onNext(t: VoteStatisticDomainModel) {
                        view.onSuccessSendVote(rowNumber, optionId, t)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        view.onErrorSendVote(ErrorHandler.getErrorMessage(context, e))
                    }
                }
        )
    }

    override fun toggleFavoriteShop(rowNumber: Int, shopId: String) {
        toggleFavoriteShop(rowNumber, 0, shopId)
    }

    override fun toggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String) {
        doFavoriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopId),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace()
                        }
                        if (!isViewAttached) return
                        view.onErrorToggleFavoriteShop(
                                ErrorHandler.getErrorMessage(context, e),
                                rowNumber,
                                adapterPosition,
                                shopId
                        )
                    }

                    override fun onNext(success: Boolean?) {
                        if (success!!) {
                            view.onSuccessToggleFavoriteShop(rowNumber, adapterPosition)
                        } else {
                            view.onErrorToggleFavoriteShop(
                                    ErrorHandler.getErrorMessage(
                                            context,
                                            RuntimeException()
                                    ),
                                    rowNumber,
                                    adapterPosition,
                                    shopId
                            )
                        }
                    }
                }
        )
    }

    private fun getFirstPageFeed(firstPageCursor: String) {
        pagingHandler.resetPage()
        viewListener.showRefresh()
        currentCursor = ""

        getDynamicFeedFirstPageUseCase.execute(
                GetDynamicFeedFirstPageUseCase.createRequestParams(
                        userId, "",
                        GetDynamicFeedUseCase.FeedV2Source.Feeds, firstPageCursor,
                        userSession.isLoggedIn),
                object : Subscriber<DynamicFeedFirstPageDomainModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace()
                        }
                        if (!isViewAttached) return
                        view.finishLoading()
                        view.onErrorGetFeedFirstPage(
                                ErrorHandler.getErrorMessage(context, e)
                        )
                        view.stopTracePerformanceMon()
                    }

                    override fun onNext(firstPageDomainModel: DynamicFeedFirstPageDomainModel) {
                        view.finishLoading()
                        view.clearData()
                        val model = firstPageDomainModel.dynamicFeedDomainModel
                        if (hasFeed(model)) {
                            view.updateCursor(model.cursor)
                            view.setLastCursorOnFirstPage(model.cursor)
                            view.setFirstPageCursor(model.firstPageCursor)
                            view.onSuccessGetFeedFirstPage(model.postList)
                            if (model.hasNext) {
                                view.setEndlessScroll()
                            } else {
                                view.unsetEndlessScroll()
                            }
                        } else {
                            view.onShowEmpty()
                        }

                        if (firstPageDomainModel.isInterestWhitelist) {
                            view.showInterestPick()
                        }

                        view.sendMoEngageOpenFeedEvent()
                        view.stopTracePerformanceMon()
                    }
                }
        )
    }

    private fun hasFeed(model: DynamicFeedDomainModel): Boolean {
        return model.postList.isNotEmpty()
    }

    private fun getNextPageFeed() {
        pagingHandler.nextPage()

        if (currentCursor.isEmpty()) {
            return
        }

        getDynamicFeedUseCase.execute(
                GetDynamicFeedUseCase.createRequestParams(
                        userId,
                        currentCursor,
                        GetDynamicFeedUseCase.FeedV2Source.Feeds),
                object : Subscriber<DynamicFeedDomainModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace()
                        }

                        if (!isViewAttached) return

                        view.unsetEndlessScroll()
                        view.onShowRetryGetFeed()
                        view.hideAdapterLoading()
                    }

                    override fun onNext(model: DynamicFeedDomainModel) {

                        view.hideAdapterLoading()

                        if (model.postList.isNotEmpty()) {
                            view.onSuccessGetFeed(model.postList)
                        }

                        if (model.hasNext) {
                            view.updateCursor(model.cursor)
                        } else {
                            view.unsetEndlessScroll()
                        }
                    }
                }
        )
    }

    override fun trackAffiliate(url: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                TrackPostClickSubscriber())
    }

    override fun addPostTagItemToCart(postTagItem: PostTagItem) {
        if (!postTagItem.shop.isEmpty()) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop[0].shopId),
                    object : Subscriber<AddToCartDataModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            if (GlobalConfig.isAllowDebuggingTools()) e.printStackTrace()
                            view.onAddToCartFailed(postTagItem.applink)
                        }

                        override fun onNext(addToCartDataModel: AddToCartDataModel) {
                            if (addToCartDataModel.data.success == 0) {
                                view.onAddToCartFailed(postTagItem.applink)
                            } else {
                                view.onAddToCartSuccess()
                            }
                        }
                    }
            )
        } else {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    override fun deletePost(id: Int, rowNumber: Int) {
        deletePostUseCase.execute(
                DeletePostUseCase.createRequestParams(id.toString()),
                object: Subscriber<Boolean>() {
                    override fun onNext(isSuccess: Boolean) {
                        if (isSuccess.not()) {
                            onError(RuntimeException())
                            return
                        }
                        view.onSuccessDeletePost(rowNumber)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        view.onErrorDeletePost(ErrorHandler.getErrorMessage(context, e), id, rowNumber)
                    }
                })
    }
}