package com.tokopedia.challenges.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class ChallengeSubmissonContractor {
    public interface View extends CustomerView {

        Activity getActivity();

        void renderSubmissionItems(SubmissionResponse submissionResponse);

        void renderChallengeDetail(Result challengeResult);

        RequestParams getSubmissionsParams();

        RequestParams getChallengeDetailsParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void hideCollapsingHeader();

        void showCollapsingHeader();

        LinearLayoutManager getLayoutManager();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void navigateToActivity(Intent intent);

        void renderTnC(TermsNCondition termsNCondition);

        void setCountDownView(String participatedText);

        void renderWinnerItems(SubmissionResponse submissionResponse);
        void finish();

        void setSnackBarErrorMessage(String message, android.view.View.OnClickListener listener);

        void setSnackBarErrorMessage(String message);

        Result getChallengeResult();

        void setChallengeResult(Result challengeResult);

        String getChallengeId();

        void setIsPastChallenge(boolean value);
    }

    public interface Presenter extends CustomerPresenter<ChallengeSubmissonContractor.View> {

        void initialize(boolean loadFromApi, Result challengeResult);

        void onSubmitButtonClick();

        void onDestroy();

        void getSubmissionChallenges(boolean loadFromApi, Result challengeResult);
    }
}
