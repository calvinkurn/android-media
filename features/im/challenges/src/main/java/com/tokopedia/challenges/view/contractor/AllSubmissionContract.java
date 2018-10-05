package com.tokopedia.challenges.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.List;

public class AllSubmissionContract {

    public interface View extends CustomerView {

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void removeFooter();

        void addFooter();

        void addSubmissionToCards(List<SubmissionResult> submissionList);

        LinearLayoutManager getLayoutManager();

        void clearList();
    }

    public interface Presenter extends CustomerPresenter<AllSubmissionContract.View> {

        void initialize();

        void onDestroy();

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        void loadMoreItems(boolean showProgress);
    }
}
