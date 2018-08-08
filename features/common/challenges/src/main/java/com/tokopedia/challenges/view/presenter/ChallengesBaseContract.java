package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;

import java.util.List;

public interface ChallengesBaseContract {
    interface View extends CustomerView {
        void setChallengeDataToUI(List<Result> resultList);
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
