package com.tokopedia.challenges.view.contractor;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.usecase.RequestParams;

public class SubmissionAdapterContract {

    public interface View extends CustomerView {

        Activity getActivity();

        RequestParams getParams();

        void notifyDataSetChanged(int position);

    }

    public interface Presenter extends CustomerPresenter<SubmissionAdapterContract.View> {

        void onDestroy();

        void setSubmissionLike(SubmissionResult result, int position);
    }
}
