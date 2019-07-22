package com.tokopedia.kol.feature.postdetail.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber;
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.subscriber.FollowUnfollowDetailSubscriber;
import com.tokopedia.kol.feature.postdetail.view.subscriber.GetKolPostDetailSubscriber;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailPresenter extends BaseDaggerPresenter<KolPostDetailContract.View>
        implements KolPostDetailContract.Presenter {

    private final GetPostDetailUseCase getPostDetailUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;
    private final FollowKolPostGqlUseCase followKolPostGqlUseCase;
    private final ToggleFavouriteShopUseCase doFavoriteShopUseCase;
    private final TrackAffiliateClickUseCase trackAffiliateClickUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final SendVoteUseCase sendVoteUseCase;
    private final UserSessionInterface userSession;
    private final AddToCartUseCase atcUseCase;

    @Inject
    public KolPostDetailPresenter(GetPostDetailUseCase getPostDetailUseCase,
                                  LikeKolPostUseCase likeKolPostUseCase,
                                  FollowKolPostGqlUseCase followKolPostGqlUseCase,
                                  ToggleFavouriteShopUseCase doFavoriteShopUseCase,
                                  SendVoteUseCase sendVoteUseCase,
                                  TrackAffiliateClickUseCase trackAffiliateClickUseCase,
                                  DeletePostUseCase deletePostUseCase,
                                  AddToCartUseCase atcUseCase,
                                  UserSessionInterface userSessionInterface) {
        this.getPostDetailUseCase = getPostDetailUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.followKolPostGqlUseCase = followKolPostGqlUseCase;
        this.doFavoriteShopUseCase = doFavoriteShopUseCase;
        this.trackAffiliateClickUseCase = trackAffiliateClickUseCase;
        this.sendVoteUseCase = sendVoteUseCase;
        this.deletePostUseCase = deletePostUseCase;
        this.atcUseCase = atcUseCase;
        this.userSession = userSessionInterface;
    }

    @Override
    public void attachView(KolPostDetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getPostDetailUseCase.unsubscribe();
        likeKolPostUseCase.unsubscribe();
        followKolPostGqlUseCase.unsubscribe();
        doFavoriteShopUseCase.unsubscribe();
        sendVoteUseCase.unsubscribe();
        trackAffiliateClickUseCase.unsubscribe();
        deletePostUseCase.unsubscribe();
    }

    @Override
    public void getCommentFirstTime(int id) {
        getView().showLoading();

        getPostDetailUseCase.execute(
                GetPostDetailUseCase.Companion.createRequestParams(
                        userSession.getUserId(),
                        "",
                        GetDynamicFeedUseCase.SOURCE_DETAIL,
                        String.valueOf(id)
                ),
                new GetKolPostDetailSubscriber(getView())
        );

    }

    @Override
    public void followKol(int id, int rowNumber) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        );
        followKolPostGqlUseCase.execute(
                new FollowUnfollowDetailSubscriber(getView(),
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_FOLLOW)
        );
    }

    @Override
    public void unfollowKol(int id, int rowNumber) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        );
        followKolPostGqlUseCase.execute(
                new FollowUnfollowDetailSubscriber(getView(),
                        rowNumber,
                        id,
                        FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        );
    }

    @Override
    public void likeKol(int id, int rowNumber, KolPostListener.View.Like likeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                new LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.ACTION_LIKE)
        );
    }

    @Override
    public void unlikeKol(int id, int rowNumber, KolPostListener.View.Like likeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                new LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.ACTION_UNLIKE)
        );
    }

    @Override
    public void toggleFavoriteShop(String shopId) {
        doFavoriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopId),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace();
                        }

                        if (!isViewAttached()) {
                            return;
                        }

                        getView().onErrorToggleFavoriteShop(
                                ErrorHandler.getErrorMessage(getView().getContext(), e),
                                shopId
                        );
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            getView().onSuccessToggleFavoriteShop();
                        } else {
                            getView().onErrorToggleFavoriteShop(
                                    ErrorHandler.getErrorMessage(
                                            getView().getContext(),
                                            new RuntimeException()
                                    ),
                                    shopId
                            );
                        }
                    }
                }
        );
    }

    @Override
    public void sendVote(int positionInFeed, String pollId, String optionId) {
        sendVoteUseCase.execute(
                SendVoteUseCase.createParamsV1(pollId, optionId),
                new Subscriber<VoteStatisticDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorSendVote(ErrorHandler.getErrorMessage(getView().getContext(), e));
                        }
                    }

                    @Override
                    public void onNext(VoteStatisticDomainModel voteStatisticDomainModel) {
                        if (getView() != null) {
                            getView().onSuccessSendVote(positionInFeed, optionId, voteStatisticDomainModel);
                        }
                    }
                });

    }

    @Override
    public void trackAffiliate(String clickURL) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.Companion.createRequestParams(clickURL), new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public void deletePost(int id, int rowNumber) {
        deletePostUseCase.execute(
                DeletePostUseCase.Companion.createRequestParams(String.valueOf(id)),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorDeletePost(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        if (getView() != null && isSuccess) {
                            getView().onSuccessDeletePost(rowNumber);
                        }
                    }
                }
        );
    }

    @Override
    public void addPostTagItemToCart(PostTagItem postTagItem) {
        if (!postTagItem.getShop().isEmpty()) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.getId(), postTagItem.getShop().get(0).getShopId()),
                    new Subscriber<AddToCartDataModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().onAddToCartFailed(postTagItem.getApplink());
                        }

                        @Override
                        public void onNext(AddToCartDataModel addToCartDataModel) {
                            getView().onAddToCartSuccess();
                        }
                    }
            );
        }
    }
}
