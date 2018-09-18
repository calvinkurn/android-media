package com.tokopedia.feedplus.view.presenter;

import android.support.annotation.RestrictTo;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil; 
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.feedplus.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFirstPageFeedsCloudUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.feedplus.domain.usecase.GetWhitelistUseCase;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.subscriber.FollowUnfollowKolSubscriber;
import com.tokopedia.feedplus.view.subscriber.FollowUnfollowKolRecommendationSubscriber;
import com.tokopedia.feedplus.view.subscriber.GetFeedsSubscriber;
import com.tokopedia.feedplus.view.subscriber.GetFirstPageFeedsSubscriber;
import com.tokopedia.feedplus.view.subscriber.LikeKolPostSubscriber;
import com.tokopedia.feedplus.view.subscriber.SendVoteSubscriber;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.kol.feature.post.domain.interactor.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.interactor.LikeKolPostUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    private final UserSession userSession;
    private final GetFeedsUseCase getFeedsUseCase;
    private final GetFirstPageFeedsUseCase getFirstPageFeedsUseCase;
    private final FavoriteShopUseCase doFavoriteShopUseCase;
    private GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase;
    private final CheckNewFeedUseCase checkNewFeedUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;
    private final FollowKolPostGqlUseCase followKolPostGqlUseCase;
    private final SendVoteUseCase sendVoteUseCase;
    private final GetWhitelistUseCase getWhitelistUseCase;
    private String currentCursor = "";
    private FeedPlus.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    FeedPlusPresenter(UserSession userSession,
                      GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase,
                      FavoriteShopUseCase favoriteShopUseCase,
                      GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase,
                      CheckNewFeedUseCase checkNewFeedUseCase,
                      LikeKolPostUseCase likeKolPostUseCase,
                      FollowKolPostGqlUseCase followKolPostGqlUseCase,
                      SendVoteUseCase sendVoteUseCase,
                      GetWhitelistUseCase whitelistUseCase) {
        this.userSession = userSession;
        this.pagingHandler = new PagingHandler();
        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsCloudUseCase = getFirstPageFeedsCloudUseCase;
        this.doFavoriteShopUseCase = favoriteShopUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;
        this.checkNewFeedUseCase = checkNewFeedUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.followKolPostGqlUseCase = followKolPostGqlUseCase;
        this.getWhitelistUseCase = whitelistUseCase;
        this.sendVoteUseCase = sendVoteUseCase;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setGetFirstPageFeedsCloudUseCase(GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase) {
        this.getFirstPageFeedsCloudUseCase = getFirstPageFeedsCloudUseCase;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public GetFirstPageFeedsCloudUseCase getGetFirstPageFeedsCloudUseCase() {
        return getFirstPageFeedsCloudUseCase;
    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsUseCase.unsubscribe();
        getFirstPageFeedsUseCase.unsubscribe();
        doFavoriteShopUseCase.unsubscribe();
        getFirstPageFeedsCloudUseCase.unsubscribe();
        checkNewFeedUseCase.unsubscribe();
        likeKolPostUseCase.unsubscribe();
        followKolPostGqlUseCase.unsubscribe();
        sendVoteUseCase.unsubscribe();
        getWhitelistUseCase.unsubscribe();
    }


    @Override
    public void fetchFirstPage() {
        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";

        if (userSession != null && userSession.isLoggedIn()) {
            getFirstPageFeedsUseCase.execute(
                    getFirstPageFeedsUseCase.getRefreshParam(userSession),
                    new GetFirstPageFeedsSubscriber(viewListener, pagingHandler.getPage()));
        } else {
            viewListener.onUserNotLogin();
        }
    }

    @Override
    public void fetchNextPage() {
        pagingHandler.nextPage();

        if (currentCursor == null)
            return;
        getFeedsUseCase.execute(
                getFeedsUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        userSession,
                        currentCursor),
                new GetFeedsSubscriber(viewListener, pagingHandler.getPage()));
    }

    public void favoriteShop(final Data promotedShopViewModel, final int adapterPosition) {
        RequestParams params = RequestParams.create();
        TKPDMapParam<String, Object> mapParam = new TKPDMapParam<>();

        AuthUtil.generateParamsNetwork2(viewListener.getActivity(),
                mapParam,
                userSession.getDeviceId(),
                userSession.getUserId());

        mapParam.put(FavoriteShopUseCase.PARAM_SHOP_ID, promotedShopViewModel.getShop().getId());
        mapParam.put(FavoriteShopUseCase.PARAM_SHOP_DOMAIN, promotedShopViewModel.getShop().getDomain());
        mapParam.put(FavoriteShopUseCase.PARAM_SRC, FavoriteShopUseCase.DEFAULT_VALUE_SRC);
        mapParam.put(FavoriteShopUseCase.PARAM_AD_KEY, promotedShopViewModel.getAdRefKey());

        params.putAll(mapParam);

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

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    @Override
    public void refreshPage() {

        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";

        if (userSession != null && userSession.isLoggedIn()) {
            getFirstPageFeedsCloudUseCase.execute(
                    getFirstPageFeedsCloudUseCase.getRefreshParam(userSession),
                    new GetFirstPageFeedsSubscriber(viewListener, pagingHandler.getPage()));
        } else {
            viewListener.onUserNotLogin();
        }
    }

    @Override
    public void checkNewFeed(String firstCursor) {
//        checkNewFeedUseCase.execute(
//                CheckNewFeedUseCase.getParam(sessionHandler, firstCursor),
//                new CheckNewFeedSubscriber(viewListener));
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
    public void sendVote(int rowNumber, String pollId, PollOptionViewModel optionViewModel) {
        sendVoteUseCase.execute(
                SendVoteUseCase.createParams(pollId, optionViewModel.getOptionId()),
                new SendVoteSubscriber(rowNumber, optionViewModel, getView())
        );
    }

    public String getUserId() {
        return userSession.getUserId();
    }

}
