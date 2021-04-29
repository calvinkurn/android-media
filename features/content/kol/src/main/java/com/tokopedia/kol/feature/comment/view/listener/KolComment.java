package com.tokopedia.kol.feature.comment.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
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

        void showMentionUserSuggestionList(List<MentionableUserViewModel> userList);

        void replyToUser(MentionableUserViewModel user);


        interface ViewHolder {
            void onGoToProfile(String url);

            void onClickMentionedProfile(String id);

            void replyToUser(MentionableUserViewModel user);

            boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition);

            void reportAction(int adapterPosition, boolean canDeleteComment, String id, String reasonType, String reasonDesc);
        }

        interface SeeAll {
            void onGoToKolComment(int rowNumber, int id);
        }
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int postId);

        void loadMoreComments(int postId);

        void updateCursor(String lastcursor);

        void deleteComment(String id, int adapterPosition);

        void sendComment(int id, String comment);

        void getMentionableUserByKeyword(String keyword);
    }
}
