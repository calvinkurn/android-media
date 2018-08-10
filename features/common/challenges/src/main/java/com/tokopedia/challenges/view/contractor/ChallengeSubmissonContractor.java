package com.tokopedia.challenges.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
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
    }

    public interface Presenter extends CustomerPresenter<ChallengeSubmissonContractor.View> {

        void initialize(boolean loadFromApi, Result challengeResult);

        void onDestroy();

    }
}
