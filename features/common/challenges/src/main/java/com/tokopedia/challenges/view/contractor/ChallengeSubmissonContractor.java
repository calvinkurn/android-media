package com.tokopedia.challenges.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

public class ChallengeSubmissonContractor {
    public interface View extends CustomerView {

        Activity getActivity();

        void renderChallengeDetail(SubmissionResponse submissionResponse);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        LinearLayoutManager getLayoutManager();

    }

    public interface Presenter extends CustomerPresenter<ChallengeSubmissonContractor.View> {

        void initialize();

        void onDestroy();

    }
}
