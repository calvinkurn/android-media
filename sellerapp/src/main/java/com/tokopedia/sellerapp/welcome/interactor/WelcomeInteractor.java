package com.tokopedia.sellerapp.welcome.interactor;

import android.content.Context;

import com.tokopedia.core.session.model.LoginProviderModel;

/**
 * Created by stevenfredian on 10/5/16.
 */

public interface WelcomeInteractor {

    void downloadProvider(Context activity, DiscoverLoginListener discoverLoginListener);

    void unSubscribe();

    interface DiscoverLoginListener{
        void onSuccess(LoginProviderModel result);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }
}
