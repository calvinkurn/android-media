package com.tokopedia.kol.feature.postdetail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel;
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailContract {
    interface View extends CustomerView {
        Context getContext();

        void showLoading();

        void dismissLoading();

        void showLoadingMore();

        void onSuccessGetKolPostDetail(List<Visitable> list, PostDetailViewModel postDetailViewModel);

        void onErrorGetKolPostDetail(String message);

        void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

        void onSuccessFollowUnfollowKol(int rowNumber);

        void stopTrace();

        void onErrorToggleFavoriteShop(String errorMessage, String shopId);

        void onSuccessToggleFavoriteShop();

        void onErrorDeletePost(Throwable e);

        void onSuccessDeletePost(int rowNumber);

        void onEmptyDetailFeed();

        void onEmptyDetailClicked();

        void onAddToCartSuccess(int positionInFeed, PostTagItem postTagItem);

        void onAddToCartFailed(String pdpAppLink);

        void onSuccessGetRelatedPost(RelatedPostViewModel relatedPostViewModel);

        void onSuccessGetWhitelist(Whitelist whitelist);

        void onSuccessGetPostStatistic(@NotNull PostStatisticCommissionUiModel statisticCommissionModel);

        void onErrorGetPostStatistic(@NotNull Throwable error, @NotNull String activityId, @NotNull List<String> productIds);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int id);

        void followKol(int id, int rowNumber);

        void unfollowKol(int id, int rowNumber);

        void likeKol(int id, int rowNumber, KolPostLikeListener likeListener);

        void unlikeKol(int id, int rowNumber, KolPostLikeListener likeListener);

        void toggleFavoriteShop(String shopId);

        void trackAffiliate(String clickURL);

        void deletePost(int id, int rowNumber);

        void addPostTagItemToCart(int positionInFeed, PostTagItem postTagItem);

        void getRelatedPost(String activityId);

        void getWhitelist();

        void getPostStatistic(@NotNull String activityId, @NotNull List<String> productIds, int likeCount, int commentCount);
    }
}
