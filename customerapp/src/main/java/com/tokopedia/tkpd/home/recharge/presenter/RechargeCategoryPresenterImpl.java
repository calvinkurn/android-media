package com.tokopedia.tkpd.home.recharge.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tokopedia.digital.widget.errorhandle.WidgetRuntimeException;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.status.Status;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractor;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;

import java.util.List;

import rx.Subscriber;

/**
 * @author kulomady 05 on 7/13/2016.
 * Modify by Nabilla Sabbaha on 8/16/2017.
 * Modified by rizkyfadillah at 10/6/17.
 */
public class RechargeCategoryPresenterImpl implements RechargeCategoryPresenter {

    private static final String TAG = RechargeCategoryPresenterImpl.class.getSimpleName();

    private Context context;
    private RechargeCategoryView view;
    private RechargeNetworkInteractor rechargeNetworkInteractor;
    private List<Category> categoryList;

    public RechargeCategoryPresenterImpl(Context context, RechargeCategoryView view,
                                         RechargeNetworkInteractor rechargeNetworkInteractor) {
        this.context = context;
        this.view = view;
        this.rechargeNetworkInteractor = rechargeNetworkInteractor;
    }

    @Override
    public void fetchDataRechargeCategory() {
        rechargeNetworkInteractor.getStatus(getStatusSubscriber());
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
                if (status != null) {
                    if (status.isMaintenance() || !isVersionMatch(status)) {
                        view.failedRenderDataRechargeCategory();
                    } else {
                        rechargeNetworkInteractor.getCategoryData(getCategoryDataSubscriber());
                    }
                }
            }
        };
    }

    private Subscriber<List<Category>> getCategoryDataSubscriber() {
        return new Subscriber<List<Category>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderErrorMessage();
            }

            @Override
            public void onNext(List<Category> data) {
                categoryList = data;
                finishPrepareRechargeModule();
            }
        };
    }

    private boolean isVersionMatch(Status status) {
        try {
            int minApiSupport = status.getMinimunAndroidBuild();
            Log.d(TAG, "version code : " + getVersionCode());
            return getVersionCode() >= minApiSupport;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void finishPrepareRechargeModule() {
        if (context != null && view != null) {
            if (categoryList != null) {
                view.renderDataRechargeCategory(categoryList);
            } else {
                view.failedRenderDataRechargeCategory();
            }
        }
    }

    private int getVersionCode() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionCode;
    }

    @Override
    public void onDestroy() {
        rechargeNetworkInteractor.onDestroy();
    }
}
