package com.tokopedia.sellerapp.welcome.view;

import android.app.Activity;

import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by stevenfredian on 10/5/16.
 */

public interface WelcomeFragmentView {
    boolean checkHasNoProvider();

    void addProgressbar();

    void removeProgressBar();

    void showProvider(List<LoginProviderModel.ProvidersBean> providerList);

    Activity getActivity();

    void onMessageError(int discoverLogin, Object... s);

    void showError(String string);

    void showProgress(boolean b);

    void setBackground(String backgroundURL);

    void hideSplash();

    void showSplash();
}
