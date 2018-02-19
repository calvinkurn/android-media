package com.tokopedia.tkpdcontent.feature.profile.view.listener;

import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

/**
 * @author by milhamj on 19/02/18.
 */

public interface KolPostListener {
    void onGoToKolProfile(int page, int rowNumber, String url);

    void onOpenKolTooltip(int page, int rowNumber, String url);

    void onFollowKolClicked(int page, int rowNumber, int id);

    void onUnfollowKolClicked(int page, int rowNumber, int id);

    void onLikeKolClicked(int page, int rowNumber, int id);

    void onUnlikeKolClicked(int page, int adapterPosition, int id);

    void onGoToKolComment(int page, int rowNumber, KolViewModel kolViewModel);

    void onGoToListKolRecommendation(int page, int rowNumber, String url);

    void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

    void onSuccessFollowUnfollowKol(int rowNumber);

    void onErrorLikeDislikeKolPost(String errorMessage);

    void onSuccessLikeDislikeKolPost(int rowNumber);

    void onFollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

    void onUnfollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

    void onSuccessFollowKolFromRecommendation(int rowNumber, int position);

    void onSuccessUnfollowKolFromRecommendation(int rowNumber, int position);
}
