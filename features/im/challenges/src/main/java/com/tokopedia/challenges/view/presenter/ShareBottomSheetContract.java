package com.tokopedia.challenges.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

public class ShareBottomSheetContract {
    public interface View extends CustomerView {
        Activity getActivity();

        void showProgress(String message);

        void hideProgress();

        Result getChallengeItem();

        SubmissionResult getSubmissionItem();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void postMapBranchUrl(String id, String branchUrl, String packageName, String title, boolean isChallenge);

        void createAndShareChallenge(String packageName);

        void createAndShareSubmission(String packageName);

        boolean getParticipatedStatus(SubmissionResult submissionResult);
    }
}
