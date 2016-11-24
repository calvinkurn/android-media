package com.tokopedia.tkpd.home.recharge.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.model.category.Category;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeDBInteractor;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeDBInteractorImpl;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractor;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeNetworkInteractorImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady 05 on 7/13/2016.
 */
public class RechargeCategoryPresenterImpl implements RechargeCategoryPresenter,
        RechargeNetworkInteractor.OnGetCategoryListener,
        RechargeNetworkInteractor.OnGetOperatorListener,
        RechargeNetworkInteractor.OnGetProductListener,
        RechargeNetworkInteractor.OnGetStatusListener,
        RechargeNetworkInteractor.OnGetRecentNumbersListener,
        RechargeNetworkInteractor.OnGetRecentOrderListener {

    static final String RECHARGE_CACHE_KEY = "PrimaryRechargeCache";
    final static String KEY_CATEGORY = "RECHARGE_CATEGORY";
    final static String KEY_STATUS = "RECHARGE_STATUS";
    final static int STATE_CATEGORY_NON_ACTIVE = 2;
    static final String KEY_LAST_ORDER = "RECHARGE_LAST_ORDER";
    private static final String TAG = RechargeCategoryPresenterImpl.class.getSimpleName();

    private Activity activity;
    private RechargeCategoryView view;
    private RechargeNetworkInteractor rechargeNetworkInteractor;
    private RechargeDBInteractor rechargeDBInteractor;
    private CategoryData categoryData;
    private final LocalCacheHandler cacheHandler;

    public RechargeCategoryPresenterImpl(Activity activity, RechargeCategoryView view) {
        this.activity = activity;
        this.view = view;
        this.rechargeNetworkInteractor = new RechargeNetworkInteractorImpl();
        this.rechargeDBInteractor = new RechargeDBInteractorImpl();
        this.cacheHandler = new LocalCacheHandler(activity, RECHARGE_CACHE_KEY);
    }

    @Override
    public void fecthDataRechargeCategory() {
        rechargeNetworkInteractor.getStatus(this);
    }

    @Override
    public void fetchRecentNumberList() {
       this.rechargeNetworkInteractor.getRecentNumbers(
               AuthUtil.generateParams(activity),this
       );
    }

    @Override
    public void fetchLastOrder() {
        this.rechargeNetworkInteractor.getLastOrder(AuthUtil.generateParams(activity),this);
    }

    private boolean isAlreadyHaveDataOnCache(String key) {
        return null != cacheHandler.getString(key);
    }

    private void storeNewDataToCache(String key, String newData) {
        cacheHandler.putString(key, newData);
        cacheHandler.applyEditor();
    }

    private String getDataOnCache(String key) {
        return cacheHandler.getString(key);
    }

    @Override
    public void onSuccess(CategoryData data) {
        categoryData = data;
        storeNewDataToCache(KEY_CATEGORY, CacheUtil.convertListModelToString(categoryData.getData(),
                new TypeToken<List<Category>>() {
                }.getType()));
        this.rechargeNetworkInteractor.getAllOperator(this);
    }

    @Override
    public void onSuccess(OperatorData data) {
        this.rechargeDBInteractor.storeOperatorData(data);
        this.rechargeNetworkInteractor.getAllProduct(this);

    }

    @Override
    public void onSuccess(ProductData data) {
        this.rechargeDBInteractor.storeProductData(data);
            finishPrepareRechargeModule();
    }

    @Override
    public void onSuccess(Status data) {
        if(SessionHandler.isV4Login(activity)){
            fetchRecentNumberList();
        }

        if (data.getData().getAttributes().getIsMaintenance()) {
            view.failedRenderDataRechargeCategory();
        } else if (!isVersionMatch(data)) {
            view.failedRenderDataRechargeCategory();
        } else {
            compareStatus(data);
        }
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

    @Override
    public void onGetRecentNumbersSuccess(RecentData recentNumber) {
       this.rechargeDBInteractor.storeRecentData(recentNumber);
    }

    @Override
    public void onGetLastOrderSuccess(LastOrder lastOrder) {
        storeNewDataToCache(KEY_LAST_ORDER,CacheUtil.convertModelToString(lastOrder,LastOrder.class));
    }

    @Override
    public void onNetworkError() {
        Log.e(TAG, "onNetworkError: ");
        this.view.renderErrorNetwork();
    }

    private void compareStatus(final Status newStatus) {
        if (isAlreadyHaveDataOnCache(KEY_STATUS)) {
            if (!getDataOnCache(KEY_STATUS)
                    .equals(CacheUtil.convertModelToString(newStatus, Status.class))) {
                startFetchNewRechargeModule();
                storeNewDataToCache(KEY_STATUS, CacheUtil.convertModelToString(newStatus, Status.class));
            } else {
                getCategoryFromCache();
            }
        } else {
            startFetchNewRechargeModule();
            storeNewDataToCache(KEY_STATUS, CacheUtil.convertModelToString(newStatus, Status.class));
        }

    }

    private void startFetchNewRechargeModule() {
        this.rechargeNetworkInteractor.getAllCategory(this);
    }

    @SuppressWarnings("unchecked")
    private void getCategoryFromCache() {
        if (isAlreadyHaveDataOnCache(KEY_CATEGORY)) {
            categoryData = new CategoryData();
            categoryData.setData((List<Category>) (Object) CacheUtil.convertStringToListModel(
                    getDataOnCache(KEY_CATEGORY),
                    new TypeToken<List<Category>>() {
                    }.getType()));
            finishPrepareRechargeModule();
        } else {
            startFetchNewRechargeModule();
        }
    }

    private void finishPrepareRechargeModule() {
        if (activity != null && view != null) {
            if (categoryData != null && !categoryData.getData().isEmpty()) {
                List<Category> categories = new ArrayList<>();
                for (Category category : categoryData.getData()) {
                    if (category.getAttributes().getStatus() != STATE_CATEGORY_NON_ACTIVE) {
                        categories.add(category);
                    }
                }
                categoryData.setData(categories);
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
}
