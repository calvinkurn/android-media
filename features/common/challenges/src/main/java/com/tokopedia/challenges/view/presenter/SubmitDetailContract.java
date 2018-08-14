package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.data.model.IndiUserModel;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

public interface SubmitDetailContract {
    public interface Presenter extends CustomerPresenter<View> {
        void setDataInFields(SubmissionResult model);

        void likeBtnClick(SubmissionResult result);
    }

    public interface View extends CustomerView {
        void showStatusInfo();

        void setProfilePic(String text);

        void setProfileText(String text);

        void setChallengeImage(String text);

        void setLikesCountView(String text);

        void setPointsView(String text);

        void setLikes(boolean text);

        void setApprovedView(String text);

        void setPendingView(String status);

        void setDeclinedView(String status);

        void setDetailTitle(String text);

        void setDetailContent(String text);

        void setParticipateTitle(String title);

        void updateLikeCount(boolean liked);
    }
}
