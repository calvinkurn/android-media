package com.tokopedia.posapp.view.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.di.CacheServiceDependencies;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.Cache;

/**
 * @author okasurya on 8/28/2017
 */
public class CacheService extends IntentService implements Cache.CallbackListener {
    private static final String ACTION_START = "com.tokopedia.posapp.view.service.action.START";

    Cache.Presenter presenter;
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
        if (intent != null
                && intent.getAction().equals(ACTION_START)
                && SessionHandler.isV4Login(this)) {

                initPresenter();
                handleActionStart();

        }
    }

    private void initPresenter() {
        presenter = new CacheServiceDependencies().provideCachePresenter(this);
        presenter.setCallbackListener(this);
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
