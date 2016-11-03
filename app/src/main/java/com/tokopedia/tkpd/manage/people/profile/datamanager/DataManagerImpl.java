package com.tokopedia.tkpd.manage.people.profile.datamanager;

import android.content.Context;

import com.tokopedia.tkpd.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.tkpd.manage.people.profile.interactor.ManagePeopleProfileInteractor;
import com.tokopedia.tkpd.manage.people.profile.interactor.ManagePeopleProfileInteractorImpl;
import com.tokopedia.tkpd.manage.people.profile.model.Profile;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.prototype.ShopSettingCache;

import org.json.JSONObject;

/**
 * Created on 6/27/16.
 */
public class DataManagerImpl implements DataManager {

    private final ManagePeopleProfileFragmentPresenter presenter;
    private final ManagePeopleProfileInteractorImpl retrofit;

    public DataManagerImpl(ManagePeopleProfileFragmentPresenter presenter) {
        this.presenter = presenter;
        this.retrofit = new ManagePeopleProfileInteractorImpl();
    }

    @Override
    public void getProfile(Context context) {
        JSONObject cache = ShopSettingCache.getSetting(ShopSettingCache.CODE_PROFILE, context);
        if (cache != null) {
//            requestCache(context);
        } else {
            requestAPI(context);
        }
    }

    @Override
    public void refreshProfile(Context context) {
        requestAPI(context);
    }

    private void requestAPI(Context context) {
        retrofit.getProfile(
                context,
                AuthUtil.generateParams(context, NetworkParam.getProfile(context)),
                new ManagePeopleProfileInteractor.GetProfileListener() {

                    @Override
                    public void onStart() {
                        presenter.setOnRequestStart();
                    }

                    @Override
                    public void onError(String error) {
                        presenter.setOnRequestError(error);
                    }

                    @Override
                    public void onTimeout(NetworkErrorHelper.RetryClickedListener listener) {
                        presenter.setOnRequestTimeOut(listener);
                    }

                    @Override
                    public void onSuccess(Profile result) {
                        presenter.setOnRequestSuccess(result);
                    }

                    @Override
                    public void onNullData() {
                        presenter.setOnRequestError(null);
                    }
                });
    }

    @Override
    public void unSubscribe() {
        retrofit.unSubscribe();
    }
}
