package com.tokopedia.sellerapp.welcome.presenter;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.sellerapp.welcome.interactor.WelcomeInteractor;
import com.tokopedia.sellerapp.welcome.interactor.WelcomeInteractorImpl;
import com.tokopedia.sellerapp.welcome.view.WelcomeFragmentView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by stevenfredian on 10/5/16.
 */

public class WelcomeFragmentPresenterImpl implements WelcomeFragmentPresenter {

    String PROVIDER_CACHE_KEY = "provider_cache";
    String BACKGROUND_CACHE_KEY = "background_cache_key";

    private WelcomeFragmentView view;
    private WelcomeInteractor interactor;
    private LocalCacheHandler providerListCache;
    private LocalCacheHandler backgroundCache;

    private String PROVIDER_LIST = "provider";
    private String BACKGROUND = "background_welcome";

    public WelcomeFragmentPresenterImpl(WelcomeFragmentView welcomeFragment) {
        view = welcomeFragment;
        interactor = WelcomeInteractorImpl.createInstance(this);
    }


    @Override
    public void initialize(Context context) {
        providerListCache = new LocalCacheHandler(context, PROVIDER_LIST);
        backgroundCache = new LocalCacheHandler(context, BACKGROUND);
    }

    @Override
    public void initData() {
        if (view.checkHasNoProvider()) {
            view.showProgress(true);
            List<LoginProviderModel.ProvidersBean> providerList = loadProvider();
            if (providerList == null || providerListCache.isExpired()) {
                downloadProviderLogin();
            } else {
                view.showProgress(false);
                view.showProvider(providerList);
                view.setBackground(getBackgroundURL());
                view.hideSplash();
            }
        }
    }


    private List<LoginProviderModel.ProvidersBean> loadProvider() {
        String cache = providerListCache.getString(PROVIDER_CACHE_KEY);
        Type type = new TypeToken<List<LoginProviderModel.ProvidersBean>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(cache, type);
    }

    private String getBackgroundURL() {
        String cache = backgroundCache.getString(BACKGROUND_CACHE_KEY);
        return cache;
    }


    private void downloadProviderLogin() {
        interactor.downloadProvider(view.getActivity(), new WelcomeInteractor.DiscoverLoginListener() {
            @Override
            public void onSuccess(LoginProviderModel result) {
                view.showProgress(false);
                view.hideSplash();
                view.showProvider(result.getProviders());
                view.setBackground(result.getUrlBackground());
                saveBackground(result.getUrlBackground());
            }

            @Override
            public void onError(String s) {
                view.showSplash();
                view.onMessageError(DownloadService.DISCOVER_LOGIN, s);
            }

            @Override
            public void onTimeout() {
                view.showSplash();
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }

            @Override
            public void onThrowable(Throwable e) {
                view.showSplash();
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }
        });
    }

    @Override
    public void destroyView() {
        interactor.unSubscribe();
    }

    @Override
    public void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider) {
        String cache = new GsonBuilder().create().toJson(loadProvider());
        String listProviderString = new GsonBuilder().create().toJson(listProvider);
        if (!cache.equals(listProviderString)) {
            providerListCache.putString(PROVIDER_CACHE_KEY, listProviderString);
            providerListCache.setExpire(3600);
            providerListCache.applyEditor();
        }
    }
    public void saveBackground(String url) {
        String cache = getBackgroundURL();
        if (url!=null && !url.equals(cache)) {
            backgroundCache.putString(BACKGROUND_CACHE_KEY, url);
            backgroundCache.setExpire(3600);
            backgroundCache.applyEditor();
        }
    }
}
