package com.tokopedia.feedplus.view.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase;
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel;
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.domain.usecase.GetDynamicFeedFirstPageUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.subscriber.FollowUnfollowKolRecommendationSubscriber;
import com.tokopedia.feedplus.view.subscriber.FollowUnfollowKolSubscriber;
import com.tokopedia.feedplus.view.subscriber.LikeKolPostSubscriber;
import com.tokopedia.feedplus.view.subscriber.SendVoteSubscriber;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.profile.view.subscriber.TrackPostClickSubscriber;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.feedplus.FeedPlusConstant.NON_LOGIN_USER_ID;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    private final UserSessionInterface userSession;
    private final ToggleFavouriteShopUseCase doFavoriteShopUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;
    private final FollowKolPostGqlUseCase followKolPostGqlUseCase;
    private final SendVoteUseCase sendVoteUseCase;
    private final GetDynamicFeedFirstPageUseCase getDynamicFeedFirstPageUseCase;
    private final GetDynamicFeedUseCase getDynamicFeedUseCase;
    private final TrackAffiliateClickUseCase trackAffiliateClickUseCase;
    private String currentCursor = "";
    private FeedPlus.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    FeedPlusPresenter(UserSessionInterface userSession,
                      ToggleFavouriteShopUseCase favoriteShopUseCase,
                      LikeKolPostUseCase likeKolPostUseCase,
                      FollowKolPostGqlUseCase followKolPostGqlUseCase,
                      SendVoteUseCase sendVoteUseCase,
                      GetDynamicFeedFirstPageUseCase getDynamicFeedFirstPageUseCase,
                      GetDynamicFeedUseCase getDynamicFeedUseCase,
                      TrackAffiliateClickUseCase trackAffiliateClickUseCase) {
        this.userSession = userSession;
        this.pagingHandler = new PagingHandler();
        this.doFavoriteShopUseCase = favoriteShopUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.followKolPostGqlUseCase = followKolPostGqlUseCase;
        this.sendVoteUseCase = sendVoteUseCase;
        this.getDynamicFeedFirstPageUseCase = getDynamicFeedFirstPageUseCase;
        this.getDynamicFeedUseCase = getDynamicFeedUseCase;
        this.trackAffiliateClickUseCase = trackAffiliateClickUseCase;
    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        doFavoriteShopUseCase.unsubscribe();
        likeKolPostUseCase.unsubscribe();
        followKolPostGqlUseCase.unsubscribe();
        sendVoteUseCase.unsubscribe();
    }

    @Override
    public void fetchFirstPage() {
        getFirstPageFeed();
    }

    @Override
    public void fetchNextPage() {
        getNextPageFeed();
    }

    @Override
    public void favoriteShop(final Data promotedShopViewModel, final int adapterPosition) {
        String PARAM_SHOP_DOMAIN = "shop_domain";
        String PARAM_SRC = "src";
        String PARAM_AD_KEY = "ad_key";
        String DEFAULT_VALUE_SRC = "fav_shop";
        RequestParams params = ToggleFavouriteShopUseCase.createRequestParam
                (promotedShopViewModel.getShop().getId());

        params.putString(PARAM_SHOP_DOMAIN, promotedShopViewModel.getShop()
                .getDomain());
        params.putString(PARAM_SRC, DEFAULT_VALUE_SRC);
        params.putString(PARAM_AD_KEY, promotedShopViewModel.getAdRefKey());

        doFavoriteShopUseCase.execute(params, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isSuccess) {
                StringBuilder stringBuilder = new StringBuilder();

                if (isSuccess) {
                    stringBuilder.append(
                            MethodChecker.fromHtml(promotedShopViewModel.getShop().getName())
                    ).append(" ");

                    if (promotedShopViewModel.isFavorit()) {
                        stringBuilder.append(
                                viewListener.getString(R.string.shop_success_unfollow)
                        );

                    } else {
                        stringBuilder.append(
                                viewListener.getString(R.string.shop_success_follow)
                        );
                    }
                } else {
                    stringBuilder.append(viewListener.getString(R.string.msg_network_error));
                }
                viewListener.showSnackbar(stringBuilder.toString());

                if (viewListener.hasFeed())
                    viewListener.updateFavorite(adapterPosition);
                else
                    viewListener.updateFavoriteFromEmpty(promotedShopViewModel.getShop().getId());
            }
        });
    }

    @Override
    public void setCursor(String cursor) {
        this.currentCursor = cursor;
    }

    @Override
    public void refreshPage() {
        getFirstPageFeed();
    }

    @Override
    public void followKol(int id, int rowNumber, FeedPlus.View.Kol kolListener) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW));
        followKolPostGqlUseCase.execute(new FollowUnfollowKolSubscriber(id, FollowKolPostGqlUseCase.PARAM_FOLLOW, rowNumber, getView(), kolListener));
    }

    @Override
    public void unfollowKol(int id, int rowNumber, FeedPlus.View.Kol kolListener) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW));
        followKolPostGqlUseCase.execute(new FollowUnfollowKolSubscriber(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber, getView(), kolListener));
    }

    @Override
    public void likeKol(int id, int rowNumber, FeedPlus.View.Kol kolListener) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                new LikeKolPostSubscriber
                        (rowNumber, getView(), kolListener));

    }

    @Override
    public void unlikeKol(int id, int rowNumber, FeedPlus.View.Kol kolListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                new LikeKolPostSubscriber(rowNumber, getView(), kolListener));
    }

    @Override
    public void followKolFromRecommendation(int id, int rowNumber, int position, FeedPlus.View.Kol kolListener) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id,
                FollowKolPostGqlUseCase.PARAM_FOLLOW));
        followKolPostGqlUseCase.execute(new FollowUnfollowKolRecommendationSubscriber(id,
                FollowKolPostGqlUseCase.PARAM_FOLLOW, rowNumber, position, getView(), kolListener));

    }

    @Override
    public void unfollowKolFromRecommendation(int id, int rowNumber, int position, FeedPlus.View.Kol kolListener) {
        followKolPostGqlUseCase.clearRequest();
        followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id,
                FollowKolPostGqlUseCase.PARAM_UNFOLLOW));
        followKolPostGqlUseCase.execute(new FollowUnfollowKolRecommendationSubscriber(id,
                FollowKolPostGqlUseCase.PARAM_UNFOLLOW, rowNumber, position, getView(), kolListener));
    }

    @Override
    public void sendVote(int rowNumber, String pollId, String optionId) {
        sendVoteUseCase.execute(
                SendVoteUseCase.createParamsV1(pollId, optionId),
                new SendVoteSubscriber(rowNumber, optionId, getView())
        );
    }

    @Override
    public void toggleFavoriteShop(int rowNumber, String shopId) {
        toggleFavoriteShop(rowNumber, 0, shopId);
    }

    @Override
    public void toggleFavoriteShop(int rowNumber, int adapterPosition, String shopId) {
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
                                rowNumber,
                                adapterPosition,
                                shopId
                        );
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            getView().onSuccessToggleFavoriteShop(rowNumber, adapterPosition);
                        } else {
                            getView().onErrorToggleFavoriteShop(
                                    ErrorHandler.getErrorMessage(
                                            getView().getContext(),
                                            new RuntimeException()
                                    ),
                                    rowNumber,
                                    adapterPosition,
                                    shopId
                            );
                        }
                    }
                }
        );
    }

    private String getUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : NON_LOGIN_USER_ID;
    }

    private void getFirstPageFeed() {
        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";

        getDynamicFeedFirstPageUseCase.execute(
                GetDynamicFeedFirstPageUseCase.Companion.createRequestParams(getUserId(), "", GetDynamicFeedUseCase.SOURCE_FEEDS, userSession.isLoggedIn()),
                new Subscriber<DynamicFeedFirstPageDomainModel>() {
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

                        getView().finishLoading();
                        getView().onErrorGetFeedFirstPage(
                                ErrorHandler.getErrorMessage(getView().getContext(), e)
                        );
                        getView().stopTracePerformanceMon();
                    }

                    @Override
                    public void onNext(DynamicFeedFirstPageDomainModel firstPageDomainModel) {
                        getView().finishLoading();
                        getView().clearData();

                        DynamicFeedDomainModel model = firstPageDomainModel
                                .getDynamicFeedDomainModel();



                        if (hasFeed(model)) {
                            getView().updateCursor(model.getCursor());
                            getView().setLastCursorOnFirstPage(model.getCursor());
                            WhitelistDomain whitelistDomain = new WhitelistDomain();
                            if (firstPageDomainModel.getWhitelistDomain() != null
                                    && firstPageDomainModel.getWhitelistDomain().isWhitelist()) {
                                whitelistDomain = firstPageDomainModel.getWhitelistDomain();
                            }
                            getView().onSuccessGetFeedFirstPage(
                                    new ArrayList<>(model.getPostList()),
                                    new WhitelistViewModel(whitelistDomain)
                            );

                            if (model.getHasNext()) {
                                getView().setEndlessScroll();
                            } else {
                                getView().unsetEndlessScroll();
                            }
                        } else {
                            getView().onShowEmpty();
                        }

                        if (firstPageDomainModel.isInterestWhitelist()) {
                            getView().showInterestPick();
                        }

                        getView().sendMoEngageOpenFeedEvent();
                        getView().stopTracePerformanceMon();
                    }
                }
        );
    }

    private void addWhitelistData(List<Visitable<?>> postList, WhitelistDomain whitelistDomain) {
        postList.add(0, new WhitelistViewModel(whitelistDomain));
    }

    private boolean hasFeed(DynamicFeedDomainModel model) {
        return model != null && !model.getPostList().isEmpty();
    }

    private void getNextPageFeed() {
        pagingHandler.nextPage();

        if (currentCursor == null) {
            return;
        }

        getDynamicFeedUseCase.execute(
                GetDynamicFeedUseCase.Companion.createRequestParams(getUserId(), currentCursor, GetDynamicFeedUseCase.SOURCE_FEEDS),
                new Subscriber<DynamicFeedDomainModel>() {
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

                        getView().unsetEndlessScroll();
                        getView().onShowRetryGetFeed();
                        getView().hideAdapterLoading();
                    }

                    @Override
                    public void onNext(DynamicFeedDomainModel model) {

                        getView().hideAdapterLoading();

                        if (model.getPostList().size() != 0) {
                            getView().onSuccessGetFeed(new ArrayList<>(model.getPostList()));
                        }

                        if (model.getHasNext()) {
                            getView().updateCursor(model.getCursor());
                        } else {
                            getView().unsetEndlessScroll();
                        }
                    }
                }
        );
    }

    @Override
    public void trackAffiliate(String url) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.Companion.createRequestParams(url),
                new TrackPostClickSubscriber());
    }
}
