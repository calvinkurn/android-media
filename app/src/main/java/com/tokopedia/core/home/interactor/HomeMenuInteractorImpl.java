package com.tokopedia.core.home.interactor;

import com.tokopedia.core.database.manager.HomeCategoryMenuDbManager;
import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;
import com.tokopedia.core.home.model.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;

import java.util.List;

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

    public HomeMenuInteractorImpl() {
        mojitoService = new MojitoService();
        subscription = new CompositeSubscription();

    }


    @Override
    public void fetchHomeCategoryMenuFromNetwork(
            Subscriber<Response<HomeCategoryMenuItem>> networksubscriber) {

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
            List<CategoryMenuModel> results = homeCategoryMenuDbManager.getDataCategoryMenu();
            listener.onSuccessFetchHomeCategoryListFromDb(results);

        } catch (Throwable throwable) {
            listener.onErrorFetchHomeCategoryListFromDb(throwable);
        }
    }

    @Override
    public void removeSubscription() {
        this.subscription.unsubscribe();
    }

}
