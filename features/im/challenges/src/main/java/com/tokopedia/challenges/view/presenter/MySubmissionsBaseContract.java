package com.tokopedia.challenges.view.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.List;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public interface MySubmissionsBaseContract {
    interface View extends CustomerView {
        void setSubmissionsDataToUI(List<SubmissionResult> resultList);

        void showErrorNetwork(String errorMessage);

        void renderEmptyList();

        void removeProgressBarView();

        void showProgressBarView();

        Context getActivity();

        void removeFooter();

        void addFooter();

        LinearLayoutManager getLayoutManager();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getMySubmissionsList(Boolean isFirst);

        void setSubmissionLike(SubmissionResult result);

        void onDestroy();
    }
}
