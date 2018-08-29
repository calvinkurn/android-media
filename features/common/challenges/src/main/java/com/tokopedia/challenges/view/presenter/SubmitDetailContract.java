package com.tokopedia.challenges.view.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

public interface SubmitDetailContract {
    public interface Presenter extends CustomerPresenter<View> {
        void setDataInFields(SubmissionResult model);

        void likeBtnClick(SubmissionResult result);

        boolean isParticipated(SubmissionResult challengeResult);
    }

    public interface View extends CustomerView {
        void showStatusInfo();

        void setProfilePic(String text);

        void setProfileText(String text);

        void setChallengeImage(String thumbnailUrl, String videoUrl);

        void setLikesCountView(String text);

        void setPointsView(String text);

        void setLikes(boolean text);

        void setApprovedView(String text);

        void setDetailTitle(String text);

        void setDetailContent(String text);

        void setParticipateTitle(String title);

        void updateLikeCount(boolean liked);

        void setSubmittResult(SubmissionResult submissionResult);

        void hidProgressBar();

        void showDialogInfo(String string, String desc);

        void navigateToActivity(Intent intent);

        void setSnackBarErrorMessage(String message);

        Activity getActivity();

        void showProgressBar();

        void setWinnerPosition(String s);

        void isParticipated(boolean participated);
    }
}
