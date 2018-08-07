package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface ChallengesBaseContract {
    interface View extends CustomerView {
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
