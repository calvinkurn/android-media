package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.List;

public interface MySubmissionsBaseContract {
    interface View extends CustomerView {
        void setSubmissionsDataToUI(List<SubmissionResult> resultList);
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
