package com.tokopedia.core.session.interactor;

import android.content.Context;

import com.tokopedia.core.session.model.LoginProviderModel;

/**
 * Created by stevenfredian on 6/21/16.
 */
public interface LoginInteractor {

    void downloadProvider(Context activity, DiscoverLoginListener discoverLoginListener);

    void unSubscribe();

    interface DiscoverLoginListener{
        void onSuccess(LoginProviderModel result);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }
}
