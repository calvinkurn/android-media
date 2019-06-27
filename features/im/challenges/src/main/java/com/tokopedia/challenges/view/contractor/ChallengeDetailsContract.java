package com.tokopedia.challenges.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.usecase.RequestParams;

import java.util.List;


/**
 * @author lalit.singh
 */
public interface ChallengeDetailsContract {

    interface View extends CustomerView {

        void showProgressBar();

        void hideProgressBar();

        //void renderSubmissionItems(SubmissionResponse submissionResponse);

        void renderChallengeDetail(Result challengeResult);

        void renderWinnerItems(SubmissionResponse submissionResponse);

        void renderTnC(TermsNCondition termsNCondition);

        void renderCountDownView(String participatedText);

        RequestParams getChallengeDetailsParams();

        RequestParams getSubmissionsParams();

        Activity getActivity();

        android.view.View getRootView();

        void setIsPastChallenge(boolean value);

        void setChallengeResult(Result challengeResult);

        String getChallengeId();

        void renderSubmissions(List<SubmissionResult> submissionResults);

        void showSubmissionListLoader();

        void hideSubmissionListLoader();

        Result getChallengeResult();

        void setSnackBarErrorMessage(String string);

        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);

        public void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent startingIntent);

        void setSnackBarErrorMessage(String message, android.view.View.OnClickListener listener);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initialize(boolean loadFromApi, Result challengeResult);

        void onSubmitButtonClick();

        void onDestroy();

        void getSubmissionChallenges(boolean loadFromApi, Result challengeResult);

    }


}
