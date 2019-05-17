package com.tokopedia.feedplus.view.listener;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus {

    interface View extends CustomerView {

        Context getContext();

        Context getActivity();

        UserSessionInterface getUserSession();

        Resources getResources();

        void sendMoEngageOpenFeedEvent();

        void stopTracePerformanceMon();

        interface Kol {

            UserSessionInterface getUserSession();

            void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

            void onSuccessFollowUnfollowKol(int rowNumber);

            void onErrorLikeDislikeKolPost(String errorMessage);

            void onSuccessLikeDislikeKolPost(int rowNumber);

            void onFollowKolFromRecommendationClicked(int rowNumber, int id, int position);

            void onUnfollowKolFromRecommendationClicked(int rowNumber, int id, int position);

            void onSuccessFollowKolFromRecommendation(int rowNumber, int position, boolean isFollow);

            void onLikeKolClicked(int rowNumber, int id);

            void onUnlikeKolClicked(int adapterPosition, int id);

            void onGoToKolComment(int rowNumber, int id);

            void onGoToLink(String link);
        }

        interface Polling {
            UserSessionInterface getUserSession();

            void onVoteOptionClicked(int rowNumber, String pollId, String optionId);

            void onGoToLink(String link);
        }

        void setLastCursorOnFirstPage(String lastCursor);

        void onOpenVideo(String videoUrl, String subtitle);

        void onInfoClicked();

        void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed);

        void onErrorGetFeedFirstPage(String errorMessage);

        void onSearchShopButtonClicked();

        void showSnackbar(String s);

        void updateFavorite(int adapterPosition);

        void showRefresh();

        void finishLoading();

        void showInterestPick();

        void updateCursor(String currentCursor);

        void onSuccessGetFeed(ArrayList<Visitable> visitables);

        void onRetryClicked();

        void onShowRetryGetFeed();

        void hideAdapterLoading();

        String getString(int msg_network_error);

        int getColor(int black);

        void onShowEmpty();

        void clearData();

        void setEndlessScroll();

        void unsetEndlessScroll();

        void onShowNewFeed(String totalData);

        void onHideNewFeed();

        boolean hasFeed();

        void updateFavoriteFromEmpty(String shopId);

        void onGoToLogin();

        void onSuccessSendVote(int rowNumber, String optionId,
                               VoteStatisticDomainModel voteStatisticDomainModel);

        void onErrorSendVote(String message);

        void onWhitelistClicked(WhitelistViewModel element);

        void onSuccessToggleFavoriteShop(int rowNumber, int adapterPosition);

        void onErrorToggleFavoriteShop(String message, int rowNumber, int adapterPosition,
                                       String shopId);
    }

    interface Presenter extends CustomerPresenter<View> {

        void fetchFirstPage();

        void fetchNextPage();

        void favoriteShop(final Data promotedShopViewModel, final int adapterPosition);

        void refreshPage();

        void setCursor(String cursor);

        void followKol(int id, int rowNumber, View.Kol kolListener);

        void unfollowKol(int id, int rowNumber, View.Kol kolListener);

        void likeKol(int id, int rowNumber, View.Kol kolListener);

        void unlikeKol(int id, int rowNumber, View.Kol kolListener);

        void sendVote(int rowNumber, String pollId, String optionId);

        void followKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);

        void unfollowKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);

        void toggleFavoriteShop(int rowNumber, String shopId);

        void toggleFavoriteShop(int rowNumber, int adapterPosition, String shopId);

        void trackAffiliate(String url);
    }
}
