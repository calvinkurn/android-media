package com.tokopedia.kol.feature.postdetail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetRelatedPostUseCase
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.subscriber.LikeKolPostSubscriber
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract
import com.tokopedia.kol.feature.postdetail.view.subscriber.FollowUnfollowDetailSubscriber
import com.tokopedia.kol.feature.postdetail.view.subscriber.GetKolPostDetailSubscriber
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel
import com.tokopedia.vote.domain.usecase.SendVoteUseCase

import java.util.ArrayList

import javax.inject.Inject

import rx.Subscriber

/**
 * @author by milhamj on 27/07/18.
 */

class KolPostDetailPresenter @Inject constructor(
        private val getPostDetailUseCase: GetPostDetailUseCase,
        private val likeKolPostUseCase: LikeKolPostUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
        private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
        private val sendVoteUseCase: SendVoteUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
        private val deletePostUseCase: DeletePostUseCase,
        private val atcUseCase: AddToCartUseCase,
        private val getRelatedPostUseCase: GetRelatedPostUseCase,
        private val getWhitelistUseCase: GetWhitelistUseCase,
        private val userSession: UserSessionInterface)
    : BaseDaggerPresenter<KolPostDetailContract.View>(), KolPostDetailContract.Presenter {

    override fun attachView(view: KolPostDetailContract.View) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        getPostDetailUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        doFavoriteShopUseCase.unsubscribe()
        trackAffiliateClickUseCase.unsubscribe()
        deletePostUseCase.unsubscribe()
        sendVoteUseCase.unsubscribe()
        atcUseCase.unsubscribe()
        getRelatedPostUseCase.unsubscribe()
        getWhitelistUseCase.unsubscribe()
    }

    override fun getCommentFirstTime(id: Int) {
        view.showLoading()

        getPostDetailUseCase.execute(
                GetPostDetailUseCase.createRequestParams(
                        userSession.userId,
                        "",
                        GetDynamicFeedUseCase.FeedV2Source.Detail,
                        id.toString()
                ),
                GetKolPostDetailSubscriber(view)
        )

    }

    override fun followKol(id: Int, rowNumber: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        )
        followKolPostGqlUseCase.execute(
                FollowUnfollowDetailSubscriber(view,
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_FOLLOW)
        )
    }

    override fun unfollowKol(id: Int, rowNumber: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        )
        followKolPostGqlUseCase.execute(
                FollowUnfollowDetailSubscriber(view,
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        )
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostLikeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like),
                LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Like)
        )
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostLikeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike),
                LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Unlike)
        )
    }

    override fun toggleFavoriteShop(shopId: String) {
        doFavoriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopId),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace()
                        }

                        if (!isViewAttached) {
                            return
                        }

                        view.onErrorToggleFavoriteShop(
                                ErrorHandler.getErrorMessage(view.context, e),
                                shopId
                        )
                    }

                    override fun onNext(success: Boolean?) {
                        if (success!!) {
                            view.onSuccessToggleFavoriteShop()
                        } else {
                            view.onErrorToggleFavoriteShop(
                                    ErrorHandler.getErrorMessage(
                                            view.context,
                                            RuntimeException()
                                    ),
                                    shopId
                            )
                        }
                    }
                }
        )
    }

    override fun sendVote(positionInFeed: Int, pollId: String, optionId: String) {
        sendVoteUseCase.execute(
                SendVoteUseCase.createParamsV1(pollId, optionId),
                object : Subscriber<VoteStatisticDomainModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (view != null) {
                            view.onErrorSendVote(ErrorHandler.getErrorMessage(view.context, e))
                        }
                    }

                    override fun onNext(voteStatisticDomainModel: VoteStatisticDomainModel) {
                        if (view != null) {
                            view.onSuccessSendVote(positionInFeed, optionId, voteStatisticDomainModel)
                        }
                    }
                })

    }

    override fun trackAffiliate(clickURL: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(clickURL), object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    e.printStackTrace()
                }
            }

            override fun onNext(aBoolean: Boolean?) {

            }
        })
    }

    override fun deletePost(id: Int, rowNumber: Int) {
        deletePostUseCase.execute(
                DeletePostUseCase.createRequestParams(id.toString()),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (view != null) {
                            view.onErrorDeletePost(e)
                        }
                    }

                    override fun onNext(isSuccess: Boolean?) {
                        if (view != null && isSuccess!!) {
                            view.onSuccessDeletePost(rowNumber)
                        }
                    }
                }
        )
    }

    override fun addPostTagItemToCart(positionInFeed: Int, postTagItem: PostTagItem) {
        val isShopEmpty = postTagItem.shop.isEmpty()
        if (!isShopEmpty) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop[0].shopId),
                    object : Subscriber<AddToCartDataModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            view.onAddToCartFailed(postTagItem.applink)
                        }

                        override fun onNext(addToCartDataModel: AddToCartDataModel) {
                            if (addToCartDataModel.data.success == 0) {
                                view.onAddToCartFailed(postTagItem.applink)
                            } else {
                                view.onAddToCartSuccess(positionInFeed, postTagItem)
                            }
                        }
                    }
            )
        } else {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    override fun getRelatedPost(activityId: String) {
        view.showLoadingMore()
        getRelatedPostUseCase.execute(
                GetRelatedPostUseCase.createRequestParams(activityId),
                object : Subscriber<FeedPostRelated>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace()
                        }
                        if (isViewNotAttached) {
                            return
                        }

                        view.dismissLoading()
                    }

                    override fun onNext(feedPostRelated: FeedPostRelated) {
                        if (isViewNotAttached) {
                            return
                        }

                        view.dismissLoading()
                        val relatedPostViewModel = RelatedPostViewModel(
                                mapRelatedPost(feedPostRelated.data)
                        )
                        view.onSuccessGetRelatedPost(relatedPostViewModel)
                    }
                }
        )
    }

    private fun mapRelatedPost(data: List<FeedPostRelated.Datum>): List<RelatedPostItemViewModel> {
        val list = ArrayList<RelatedPostItemViewModel>()
        for (item in data) {
            list.add(RelatedPostItemViewModel(item))
        }
        return list
    }

    override fun getWhitelist() {
        getWhitelistUseCase.clearRequest()
        getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_ENTRY_POINT))
        )
        getWhitelistUseCase.execute(RequestParams.EMPTY, object: Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse) {
                val query = t.getData<WhitelistQuery>(WhitelistQuery::class.java)
                val whitelist = query?.whitelist ?: Whitelist()
                view?.onSuccessGetWhitelist(whitelist)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.debugTrace()

            }
        })
    }
}
