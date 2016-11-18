/*
 * Created By Kulomady on 10/4/16 11:00 AM
 * Copyright (c) 2016. All Rights Reserved
 *
 * Last Modified 10/4/16 10:59 AM
 */

package com.tokopedia.core.home.presenter;

import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.database.manager.HomeCategoryMenuDbManager;
import com.tokopedia.core.home.HomeCatMenuView;
import com.tokopedia.core.home.interactor.HomeMenuInteractor;
import com.tokopedia.core.home.interactor.HomeMenuInteractorImpl;
import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;
import com.tokopedia.core.home.model.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.core.home.model.homeMenu.LayoutSection;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;

/**
 * @author Kulomady on 10/4/16.
 */

public class HomeCatMenuPresenterImpl implements HomeCatMenuPresenter,
        HomeMenuInteractor.OnFetchHomeCategoryMenuFromDbListener, ErrorListener {

    public static final String MARKETPLACE_TYPE = "Marketplace";
    public static final String DIGITAL_TYPE = "Digital";
    private static final String TAG = "HomeCatmenuPresenter";
    private final HomeMenuInteractor homeMenuInteractor;
    private HomeCatMenuView view;
    HomeCategoryMenuDbManager dbManager;


    public HomeCatMenuPresenterImpl(HomeCatMenuView view) {
        this.view = view;
        homeMenuInteractor = new HomeMenuInteractorImpl();
        dbManager = new HomeCategoryMenuDbManager();
    }


    @Override
    public void fetchHomeCategoryMenu(boolean isFromRetry) {
        Subscriber<Response<HomeCategoryMenuItem>> subscriber = getSubcribption();
        homeMenuInteractor.fetchHomeCategoryMenuFromDb(this);
        if (dbManager.isExpired(System.currentTimeMillis())) {
            homeMenuInteractor.fetchHomeCategoryMenuFromNetwork(subscriber);
        }
    }

    @Override
    public void OnDestroy() {
        this.homeMenuInteractor.removeSubscription();
        this.view = null;
    }


    @Override
    public void onSuccessFetchHomeCategoryListFromDb(
            List<CategoryMenuModel> categoryMenuModelList) {

        renderHomeCategoryMenu(categoryMenuModelList);
    }

    @Override
    public void onErrorFetchHomeCategoryListFromDb(Throwable throwable) {
        if (isViewNotNull()) view.showGetCatMenuFromDbErrorMessage(R.string.error_home_menu);
    }

    @Override
    public void onUnknown() {
        if (isAlreadyHaveDataOnCache()) {
            getDataFromCacheAndDisplaIt();
        } else {
            if (isViewNotNull()) view.showGetCatMenuErrorMessage(R.string.error_home_menu);
        }
    }

    @Override
    public void onTimeout() {
        if (isAlreadyHaveDataOnCache()) {
            getDataFromCacheAndDisplaIt();
        } else {
            if (isViewNotNull()) view.showGetCatMenuErrorMessage(R.string.error_home_menu);
        }

    }

    @Override
    public void onServerError() {
        if (isAlreadyHaveDataOnCache()) {
            getDataFromCacheAndDisplaIt();
        } else {
            if (isViewNotNull()) view.showGetCatMenuErrorMessage(R.string.error_home_menu);
        }
    }

    @Override
    public void onBadRequest() {
        if (isAlreadyHaveDataOnCache()) {
            getDataFromCacheAndDisplaIt();
        } else {
            if (isViewNotNull()) view.showGetCatMenuErrorMessage(R.string.error_home_menu);
        }
    }

    @Override
    public void onForbidden() {
        if (isAlreadyHaveDataOnCache()) {
            getDataFromCacheAndDisplaIt();
        } else {
            if (isViewNotNull()) view.showGetCatMenuErrorMessage(R.string.error_home_menu);
        }
    }

    private Subscriber<Response<HomeCategoryMenuItem>> getSubcribption() {
        return new Subscriber<Response<HomeCategoryMenuItem>>() {

            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
                handleErrorWhenGetHomeCatMenu(e);
            }

            @Override
            public void onNext(Response<HomeCategoryMenuItem> response) {
                HomeCategoryMenuItem homeCategoryResponse = response.body();
                if (homeCategoryResponse != null && homeCategoryResponse.getData() != null) {
                    saveToCacheAndDisplayApiResponse(response, homeCategoryResponse);
                } else {
                    new ErrorHandler(HomeCatMenuPresenterImpl.this, response.code());

                }

            }
        };
    }

    private void saveToCacheAndDisplayApiResponse(Response<HomeCategoryMenuItem> response,
                                                  HomeCategoryMenuItem homeCategoryResponse) {

        List<LayoutSection> categoryMenuItemsList =
                homeCategoryResponse.getData().getLayoutSections();

        if (response.isSuccessful() && categoryMenuItemsList.size() > 0) {
            //only for testing
            HomeCategoryMenuDbManager homeCategoryMenuDbManager = new HomeCategoryMenuDbManager();
            homeCategoryMenuDbManager.store(response.body());
            renderHomeCategoryMenu(homeCategoryMenuDbManager.getDataCategoryMenu());
        } else {
            if (isViewNotNull())
                view.showGetCatMenuEmptyMessage(R.string.error_empty_home_menu);
        }
    }

    private void handleErrorWhenGetHomeCatMenu(Throwable e) {
        if (e instanceof UnknownHostException) {
            if (isAlreadyHaveDataOnCache()) {
                getDataFromCacheAndDisplaIt();
            } else {
                view.showGetCatMenuUnknownHostMessage(R.string.error_home_menu);
            }
        } else if (e instanceof SocketTimeoutException) {
            if (isAlreadyHaveDataOnCache()) {
                getDataFromCacheAndDisplaIt();
            } else {
                view.showGetCatMenuSocketTimeoutExceptionMessage(R.string.error_home_menu);
            }
        } else {
            if (isAlreadyHaveDataOnCache()) {
                getDataFromCacheAndDisplaIt();
            } else {
                view.showGetCatMenuErrorMessage(R.string.error_home_menu);
            }
        }
    }


    private boolean isAlreadyHaveDataOnCache() {
        return dbManager.getDataCategoryMenu() != null && dbManager.getDataCategoryMenu().size() > 0;
    }

    private void getDataFromCacheAndDisplaIt() {
        List<CategoryMenuModel> dataFromCache = dbManager.getDataCategoryMenu();
        renderHomeCategoryMenu(dataFromCache);
    }

    private void renderHomeCategoryMenu(List<CategoryMenuModel> menuModels) {
        if (isViewNotNull()) {
            view.renderHomeCatMenu((ArrayList<CategoryMenuModel>) menuModels);
        }
    }

    private boolean isViewNotNull() {
        return view != null;
    }

}
