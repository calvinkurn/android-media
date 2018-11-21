package com.tokopedia.challenges.view.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Collection;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public interface SubmitDetailContract {
    interface Presenter extends CustomerPresenter<View> {
        void setDataInFields(SubmissionResult model);

        void likeBtnClick(SubmissionResult result);

        void sendBuzzPointEvent(String submissionId);

        void getSubmissionDetails(String submissionId);

        void onSubmitButtonClick(String challegeId);

        void deleteSubmittedPost(String submissionId, String challengeId, String challengeName);

        boolean getParticipatedStatus(SubmissionResult challengeResult);

        boolean checkIsPastChallenge(Collection collection);

        void onDestroy();
    }

    interface View extends CustomerView {

        void showStatusInfo(String statusMessage, boolean show);

        void setProfilePic(String text);

        void setProfileText(String text);

        void setChallengeImage(String thumbnailUrl, String videoUrl);

        void setLikesCountView(String text);

        void setPointsView(String text);

        void setLikes(boolean text);

        void setApprovedView(String text, String message);

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

        void setResubmitInvisible();
    }
}
