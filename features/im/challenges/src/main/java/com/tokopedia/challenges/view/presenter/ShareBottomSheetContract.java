package com.tokopedia.challenges.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class ShareBottomSheetContract {
    public interface View extends CustomerView {
        Activity getActivity();

        void showProgress(String message);

        void hideProgress();

        Result getChallengeItem();

        SubmissionResult getSubmissionItem();
    }

    public interface Presenter extends CustomerPresenter<View> {

        void createAndShareChallenge(String packageName, String name);

        void createAndShareSubmission(String packageName, String name);

        boolean getParticipatedStatus(SubmissionResult submissionResult);
    }
}
