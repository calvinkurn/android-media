package com.tokopedia.affiliate.feature.onboarding.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * @author by milhamj on 10/4/18.
 */
public interface UsernameInputContract {
    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccessGetUsernameSuggestion(List<String> suggestions);
    }
    interface Presenter extends CustomerPresenter<View> {
        void getUsernameSuggestion();
    }
}
