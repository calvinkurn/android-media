package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.home.database.HomeCategoryMenuDbManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Kulomady on 10/3/16.
 */

public class HomeMenuInteractorImpl implements HomeMenuInteractor {

    private final CompositeSubscription subscription;
    private final MojitoService mojitoService;
    private final AceSearchService aceSearchService;

    public HomeMenuInteractorImpl() {
        mojitoService = new MojitoService();
        subscription = new CompositeSubscription();
        aceSearchService = new AceSearchService();
    }


    @Override
    public void fetchHomeCategoryMenuFromNetwork(
            Subscriber<Response<String>> networksubscriber) {

        subscription.add(mojitoService.getApi().getHomeCategoryMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        networksubscriber
                ));
    }

    @Override
    public void fetchHomeCategoryMenuFromDb(OnFetchHomeCategoryMenuFromDbListener listener) {
        HomeCategoryMenuDbManager homeCategoryMenuDbManager = new HomeCategoryMenuDbManager();
        try {
            List<CategoryMenuModel> results = homeCategoryMenuDbManager.getDataHomeCategoryMenu();
            listener.onSuccessFetchHomeCategoryListFromDb(results);

        } catch (Throwable throwable) {
            listener.onErrorFetchHomeCategoryListFromDb(throwable);
        }
    }

    @Override
    public void removeSubscription() {
        this.subscription.unsubscribe();
    }

    @Override
    public void fetchTopPicksNetworkNetwork(Map<String, String> params, Subscriber<Response<String>> networksubscriber) {
        subscription.add(aceSearchService.getApi().getTopPicks( params,BuildConfig.VERSION_NAME,"android").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        networksubscriber
                ));
    }

}
