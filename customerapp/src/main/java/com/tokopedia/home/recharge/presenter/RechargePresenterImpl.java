package com.tokopedia.home.recharge.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.model.RechargeOperatorModelDB;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.home.recharge.interactor.RechargeDBInteractor;
import com.tokopedia.home.recharge.interactor.RechargeDBInteractorImpl;
import com.tokopedia.home.recharge.interactor.RechargeNetworkInteractor;
import com.tokopedia.home.recharge.interactor.RechargeNetworkInteractorImpl;
import com.tokopedia.home.recharge.view.RechargeView;

import java.util.List;


/**
 * @author Kulomady 05 on 7/13/2016.
 */
public class RechargePresenterImpl implements RechargePresenter,
        RechargeNetworkInteractor.OnGetOperatorListener,
        RechargeNetworkInteractor.OnGetProductListener,
        RechargeDBInteractor.OnGetListProduct,
        RechargeDBInteractor.OnGetOperatorByIdListener, RechargeDBInteractor.OnGetRecentNumberListener {

    private static final String RECHARGE_PHONEBOOK_CACHE_KEY = "RECHARGE_CACHE";
    private final LocalCacheHandler cacheHandlerPhoneBook;
    private final LocalCacheHandler cacheHandlerLastOrder;
    private RechargeView view;
    private RechargeNetworkInteractor interactor;
    private RechargeDBInteractor dbInteractor;
    private Context context;

    public RechargePresenterImpl(Context context, RechargeView view) {
        this.view = view;
        this.interactor = new RechargeNetworkInteractorImpl();
        this.dbInteractor = new RechargeDBInteractorImpl();
        this.context = context;
        this.cacheHandlerPhoneBook = new LocalCacheHandler(this.context, RECHARGE_PHONEBOOK_CACHE_KEY);
        this.cacheHandlerLastOrder = new LocalCacheHandler(
                this.context, RechargeCategoryPresenterImpl.RECHARGE_CACHE_KEY
        );
    }

    @Override
    public void fetchDataProducts() {
        this.interactor.getAllProduct(this);
    }

    @Override
    public void fetchRecentNumbers(int categoryId) {
        dbInteractor.getRecentData(categoryId,this);
    }

    @Override
    public void validatePhonePrefix(String phonePrefix, int categoryId, final Boolean validatePrefix) {
        dbInteractor.getListProduct(this, phonePrefix, categoryId, validatePrefix);
    }

    @Override
    public boolean isAlreadyHavePhonebookDataOnCache(String key) {
        return null != cacheHandlerPhoneBook.getString(key);
    }

    @Override
    public boolean isAlreadyHaveLastOrderDataOnCache() {
        return null != cacheHandlerLastOrder.getString(RechargeCategoryPresenterImpl.KEY_LAST_ORDER);
    }

    @Override
    public LastOrder getLastOrderFromCache() {
        if (isAlreadyHaveLastOrderDataOnCache()) {
            String temp = cacheHandlerLastOrder.getString(RechargeCategoryPresenterImpl.KEY_LAST_ORDER);
            return CacheUtil.convertStringToModel(temp, LastOrder.class);
        } else {
            return null;
        }
    }

    @Override
    public void saveLastInputToCache(String key, String userLastInput) {
        cacheHandlerPhoneBook.putString(key, userLastInput);
        cacheHandlerPhoneBook.applyEditor();
    }

    @Override
    public String getLastInputFromCache(String key) {
        return cacheHandlerPhoneBook.getString(key);
    }

    @Override
    public void clearRechargePhonebookCache() {
        LocalCacheHandler.clearCache(this.context, RECHARGE_PHONEBOOK_CACHE_KEY);

    }
    @Override
    public void onSuccess(OperatorData data) {
        this.fetchDataProducts();
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onSuccess(ProductData data) {
        if (this.view != null) {
            this.view.hideProgressFetchData();
        }
    }

    @Override
    public void onSuccess(List<Product> listProduct) {
        if (!listProduct.isEmpty()) {
            String operatorId = String.valueOf(
                    listProduct.get(0).getRelationships().getOperator().getData().getId()
            );
            dbInteractor.getOperatorById(operatorId, this);
            view.renderDataProducts(listProduct);
        }
    }

    @Override
    public void onGetRecentNumberSuccess(List<String> results) {
        view.renderDataRecent(results);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderDataProductsEmpty("Product List is Empty");
    }

    @Override
    public void onSuccess(RechargeOperatorModelDB operator) {
        view.showImageOperator(operator.image);
    }

    @Override
    public void onEmpty() {

    }

}
