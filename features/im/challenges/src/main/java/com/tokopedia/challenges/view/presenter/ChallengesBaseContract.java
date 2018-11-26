package com.tokopedia.challenges.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;

import java.util.List;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public interface ChallengesBaseContract {
    interface View extends CustomerView {
        void setChallengeDataToUI(List<Result> resultList, boolean isPastChallenge);

        void showErrorNetwork(String errorMessage);

        void renderEmptyList();

        void removeProgressBarView();

        void showProgressBarView();

        Context getActivity();

        List<Result> getOpenChallenges();

        List<Result> getPastChallenges();

        void setSwipeRefreshing();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getOpenChallenges();

        void getPastChallenges();

        void onDestroy();
    }
}
