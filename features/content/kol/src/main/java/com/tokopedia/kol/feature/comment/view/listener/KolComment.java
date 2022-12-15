package com.tokopedia.kol.feature.comment.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;

import java.util.List;

/**
 * @author by nisie on 10/31/17.
 */

public interface KolComment {

    interface View extends CustomerView {

        Context getContext();

        void openRedirectUrl(String url);

        void showLoading();

        void onErrorGetCommentsFirstTime(String errorMessage);

        void onServerErrorGetCommentsFirstTime(String errorMessage);

        void onSuccessGetCommentsFirstTime(KolComments kolComments);

        void removeLoading();

        void loadMoreComments();

        void onSuccessGetComments(KolComments kolComments);

        void onSuccessChangeWishlist();

        void updateCursor(String lastcursor);

        void onErrorLoadMoreComment(String errorMessage);

        void onErrorSendComment(String errorMessage);

        void onSuccessSendComment(SendKolCommentDomain sendKolCommentDomain);

        void dismissProgressDialog();

        void showProgressDialog();

        void onErrorDeleteComment(String errorMessage);

        void onSuccessDeleteComment(int adapterPosition);

        void enableSendComment();

        void disableSendComment();

        void showMentionUserSuggestionList(List<MentionableUserModel> userList);

        void replyToUser(MentionableUserModel user);

        void onHashTagClicked(String hashTag);

        void onSuccessSendReport();

        void onErrorSendReport(String message);


        interface ViewHolder {
            void onGoToProfile(String url, String userId);

            void onClickMentionedProfile(String id);

            void replyToUser(MentionableUserModel user);

            boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition);

            void onMenuClicked(String id, boolean canDeleteComment, int adapterPosition);
        }

        interface SeeAll {
            void onGoToKolComment(int rowNumber, int id);
        }
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(long postId);

        void loadMoreComments(long postId);

        void updateCursor(String lastcursor);

        void deleteComment(String id, int adapterPosition);

        void sendComment(long id, String comment);

        void getMentionableUserByKeyword(String keyword);

        void sendReport(int contentId, String reasonType, String reasonMessage, String contentType);

    }
}
