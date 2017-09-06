package com.tokopedia.posapp.view.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.Cache;
import com.tokopedia.posapp.view.presenter.CachePresenter;
import com.tokopedia.posapp.di.component.DaggerPosCacheComponent;

import javax.inject.Inject;

/**
 * @author okasurya on 8/28/2017
 */
public class CacheService extends IntentService implements Cache.CallbackListener {
    private static final String ACTION_START = "com.tokopedia.posapp.view.service.action.START";

    @Inject
    CachePresenter presenter;

    GetProductListUseCase getProductListUseCase;
    StoreProductCacheUseCase storeProductCacheUseCase;

    public CacheService() {
        super("CacheService");
    }

    public static Intent getServiceIntent(Context context) {
        Intent intent = new Intent(context, CacheService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initInjection();

        if (intent != null
                && intent.getAction().equals(ACTION_START)
                && SessionHandler.isV4Login(this)) {

                presenter.setCallbackListener(this);
                handleActionStart();

        }
    }

    private void initInjection() {
        AppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        DaggerPosCacheComponent daggerCacheComponent =
                (DaggerPosCacheComponent) DaggerPosCacheComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCacheComponent.inject(this);
    }

    private void handleActionStart() {
        presenter.getData();
    }

    @Override
    public void onProductListStored() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
