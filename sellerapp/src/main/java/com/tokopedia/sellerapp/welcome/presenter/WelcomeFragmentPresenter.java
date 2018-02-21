package com.tokopedia.sellerapp.welcome.presenter;

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
}
