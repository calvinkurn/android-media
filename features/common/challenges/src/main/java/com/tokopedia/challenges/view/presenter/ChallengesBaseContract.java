package com.tokopedia.challenges.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;

import java.util.List;

public interface ChallengesBaseContract {
    interface View extends CustomerView {
        void setChallengeDataToUI(List<Result> resultList);

        void showErrorNetwork(String errorMessage);

        void renderEmptyList();

        void removeProgressBarView();

        void showProgressBarView();

        Context getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
