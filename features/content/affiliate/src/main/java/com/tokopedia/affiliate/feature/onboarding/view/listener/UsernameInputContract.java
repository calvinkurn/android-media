package com.tokopedia.affiliate.feature.onboarding.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * @author by milhamj on 10/4/18.
 */
public interface UsernameInputContract {
    interface View extends CustomerView {
        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetUsernameSuggestion(List<String> suggestions);

        void onSuggestionClicked(String username);

        void onSuccessRegisterUsername();

        void onErrorRegisterUsername(String message);

        void onErrorInputRegisterUsername(String message);
    }
    interface Presenter extends CustomerPresenter<View> {
        void getUsernameSuggestion();

        void registerUsername(String username);
    }
}
