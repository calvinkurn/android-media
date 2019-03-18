package com.tokopedia.kol.feature.post.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

/**
 * @author by milhamj on 19/02/18.
 */

public interface KolPostListener {
    interface View extends CustomerView {
        Context getContext();

        KolRouter getKolRouter();

        AbstractionRouter getAbstractionRouter();

        UserSessionInterface getUserSession();

        void showLoading();

        void hideLoading();

        void onSuccessGetProfileData(List<Visitable> visitableList);

        void onEmptyKolPost();

        void onErrorGetProfileData(String message);

        void updateCursor(String lastCursor);

        void onSuccessDeletePost(int rowNumber);

        void onErrorDeletePost(String message, int rowNumber, int id);

        interface Like {
            Context getContext();

            void onLikeKolSuccess(int rowNumber);

            void onLikeKolError(String message);
        }

        interface ViewHolder {
            Context getContext();

            UserSessionInterface getUserSession();

            void onGoToKolProfile(int rowNumber, String userId, int postId);

            void onGoToKolProfileUsingApplink(int rowNumber, String applink);

            void onOpenKolTooltip(int rowNumber, String uniqueTrackingId, String url);

            void trackContentClick(boolean hasMultipleContent, String activityId,
                                   String activityType, String position);

            void trackTooltipClick(boolean hasMultipleContent, String activityId,
                                   String activityType, String position);

            void onFollowKolClicked(int rowNumber, int id);

            void onUnfollowKolClicked(int rowNumber, int id);

            void onLikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                  String activityType);

            void onUnlikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                    String activityType);

            void onGoToKolComment(int rowNumber, int id, boolean hasMultipleContent,
                                  String activityType);

            void onEditClicked(boolean hasMultipleContent, String activityId,
                               String activityType);

            void onMenuClicked(int rowNumber, BaseKolViewModel element);
        }
    }

    interface Presenter extends CustomerPresenter<View> {
        void initView(String userId);

        void getKolPost(String userId);

        void updateCursor(String lastCursor);

        void followKol(int id, int rowNumber, View kolListener);

        void unfollowKol(int id, int rowNumber, View kolListener);

        void likeKol(int id, int rowNumber, View.Like likeListener);

        void unlikeKol(int id, int rowNumber, View.Like likeListener);

        void deletePost(int rowNumber, int id);
    }
}
