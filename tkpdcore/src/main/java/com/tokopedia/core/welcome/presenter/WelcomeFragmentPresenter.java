package com.tokopedia.core.welcome.presenter;

import android.content.Context;

import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by stevenfredian on 10/5/16.
 */

public interface WelcomeFragmentPresenter {
    void initData();

    void initialize(Context activity);

    void destroyView();

    void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider);

    void loginFacebook(Context context);

    void loginGoogle(Context context);

    void loginWebview(Context context, String url, String name);
}
