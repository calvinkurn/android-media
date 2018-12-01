package com.tokopedia.feedplus.view.listener;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.user.session.UserSession;
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

        void eventTrackingEEGoToProduct(Integer shopId, String feedId, int totalProduct, int positionInFeed, String category);

        void sendMoEngageOpenFeedEvent();

        interface Kol {

            UserSessionInterface getUserSession();

            void onGoToKolProfileFromRecommendation(int position, int itemPosition, String userId);

            void onGoToListKolRecommendation(int page, int rowNumber, String url);

            void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

            void onSuccessFollowUnfollowKol(int rowNumber);

            void onErrorLikeDislikeKolPost(String errorMessage);

            void onSuccessLikeDislikeKolPost(int rowNumber);

            void onFollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

            void onUnfollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

            void onSuccessFollowKolFromRecommendation(int rowNumber, int position);

            void onSuccessUnfollowKolFromRecommendation(int rowNumber, int position);

            void onLikeKolClicked(int rowNumber, int id);

            void onUnlikeKolClicked(int adapterPosition, int id);

            void onGoToKolComment(int rowNumber, int id);

            void onGoToLink(String link);
        }

        interface Polling {
            UserSessionInterface getUserSession();

            void onVoteOptionClicked(int rowNumber, String pollId,
                                     PollOptionViewModel optionViewModel);

            void onGoToLink(String link);

            void trackEEPoll(PollOptionViewModel element, String trackingPromoCode, int rowNumber, PollViewModel pollViewModel);
        }

        void setFirstCursor(String firstCursor);

        void setLastCursorOnFirstPage(String lastCursor);

        void onGoToProductDetail(int rowNumber, int page, String id, String imageSourceSingle, String name, String productId);

        void onGoToProductDetailFromProductUpload(
                int rowNumber,
                int positionFeedCard,
                int page,
                int itemPosition,
                String productId,
                String imageSourceSingle,
                String name,
                String price,
                String priceInt,
                String productUrl,
                String eventLabel
        );

        void onGoToProductDetailFromRecentView(String productID, String imgUri, String name, String price);

        void onGoToProductDetailFromInspiration(int page, int rowNumber, String productId,
                                                String imageSource, String name, String price, String priceInt,
                                                String productUrl, String source, int positionFeedCard, int itemPosition, String eventLabel);

        void onGoToFeedDetail(int page, int rowNumber, String feedId);

        void onGoToShopDetail(int page, int rowNumber, Integer shopId, String url);

        void onOpenVideo(String videoUrl, String subtitle);

        void onInfoClicked();

        void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed);

        void onErrorGetFeedFirstPage(String errorMessage);

        void onSearchShopButtonClicked();

        void onFavoritedClicked(int adapterPosition);

        void showSnackbar(String s);

        void updateFavorite(int adapterPosition);

        void showRefresh();

        void finishLoading();

        void showInterestPick();

        void updateCursor(String currentCursor);

        void onSuccessGetFeed(ArrayList<Visitable> visitables);

        void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeedView);

        void onRetryClicked();

        void onShowRetryGetFeed();

        void onShowAddFeedMore();

        void shouldLoadTopAds(boolean loadTopAds);

        void hideTopAdsAdapterLoading();

        String getString(int msg_network_error);

        int getColor(int black);

        void onShowEmpty();

        void clearData();

        void unsetEndlessScroll();

        void onShowNewFeed(String totalData);

        void onHideNewFeed();

        boolean hasFeed();

        void updateFavoriteFromEmpty(String shopId);

        void onBrandClicked(int page, int rowNumber, OfficialStoreViewModel officialStoreViewModel);

        void onSeeAllOfficialStoresFromCampaign(int page, int rowNumber, String redirectUrl);

        void onGoToCampaign(int page, int rowNumber, String redirectUrl, String title);

        void onSeeAllOfficialStoresFromBrands(int page, int rowNumber);

        void onGoToProductDetailFromCampaign(int page, int rowNumber, String productId,
                                             String imageSourceSingle, String name, String price);

        void onGoToShopDetailFromCampaign(int page, int rowNumber, String shopUrl);

        void onContentProductLinkClicked(String url);

        void onUserNotLogin();

        void onGoToLogin();

        void onSuccessSendVote(int rowNumber, PollOptionViewModel optionViewModel,
                               VoteStatisticDomainModel voteStatisticDomainModel);

        void onErrorSendVote(String message);

        int getAdapterListSize();

        void onWhitelistClicked(String url);
    }

    interface Presenter extends CustomerPresenter<View> {

        void fetchFirstPage();

        void fetchNextPage();

        void refreshPage();

        void checkNewFeed(String cursor);

        void followKol(int id, int rowNumber, View.Kol kolListener);

        void unfollowKol(int id, int rowNumber, View.Kol kolListener);

        void likeKol(int id, int rowNumber, View.Kol kolListener);

        void unlikeKol(int id, int rowNumber, View.Kol kolListener);

        void sendVote(int rowNumber, String pollId, PollOptionViewModel optionViewModel);

        void followKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);

        void unfollowKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);
    }
}
