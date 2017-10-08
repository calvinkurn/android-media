package com.tokopedia.tkpd.home.recharge.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.widget.errorhandle.WidgetRuntimeException;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractor;
import com.tokopedia.tkpd.home.recharge.util.CategoryComparator;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;

import java.util.Collections;

import rx.Subscriber;

/**
 * @author kulomady 05 on 7/13/2016.
 * Modify by Nabilla Sabbaha on 8/16/2017.
 * Modified by rizkyfadillah at 10/6/17.
 */
public class RechargeCategoryPresenterImpl implements RechargeCategoryPresenter {

    private static final String TAG = RechargeCategoryPresenterImpl.class.getSimpleName();

    private Activity activity;
    private RechargeCategoryView view;
    private RechargeNetworkInteractor rechargeNetworkInteractor;
    private CategoryData categoryData;

    public RechargeCategoryPresenterImpl(Activity activity, RechargeCategoryView view,
                                         RechargeNetworkInteractor rechargeNetworkInteractor) {
        this.activity = activity;
        this.view = view;
        this.rechargeNetworkInteractor = rechargeNetworkInteractor;
    }

    @Override
    public void fetchDataRechargeCategory() {
        rechargeNetworkInteractor.getStatus(getStatusSubscriber());
    }

    @Override
    public void fetchStatusDigitalProductData() {
        rechargeNetworkInteractor.getStatusResume(getStatusSubscriber());
    }

    private Subscriber<Status> getStatusSubscriber() {
        return new Subscriber<Status>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof WidgetRuntimeException) {
                    view.renderErrorMessage();
                } else {
                    view.renderErrorNetwork();
                }
            }

            @Override
            public void onNext(Status status) {
                SessionHandler sessionHandler = new SessionHandler(activity);
                if (status != null) {
                    if (sessionHandler.isV4Login(activity)) {
                    }
                    if (status.getData().getAttributes().getIsMaintenance() || !isVersionMatch(status)) {
                        view.failedRenderDataRechargeCategory();
                    } else {
                        rechargeNetworkInteractor.getCategoryData(getCategoryDataSubscriber());
                    }
                }
            }
        };
    }

    private Subscriber<CategoryData> getCategoryDataSubscriber() {
        return new Subscriber<CategoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderErrorMessage();
            }

            @Override
            public void onNext(CategoryData data) {
                categoryData = data;
                finishPrepareRechargeModule();
            }
        };
    }

    public void onNetworkError() {
        view.renderErrorNetwork();
    }

    private boolean isVersionMatch(Status status) {
        try {
            int minApiSupport = Integer.parseInt(
                    status.getData().getAttributes().getVersion().getMinimumAndroidBuild()
            );
            Log.d(TAG, "version code : " + getVersionCode());
            return getVersionCode() >= minApiSupport;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void finishPrepareRechargeModule() {
        if (activity != null && view != null) {
            if (categoryData != null) {
                Collections.sort(categoryData.getData(), new CategoryComparator());
                view.renderDataRechargeCategory(categoryData);
            } else {
                view.failedRenderDataRechargeCategory();
            }
        }
    }

    private int getVersionCode() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        return pInfo.versionCode;
    }

    @Override
    public void onDestroy() {
        rechargeNetworkInteractor.onDestroy();
    }
}