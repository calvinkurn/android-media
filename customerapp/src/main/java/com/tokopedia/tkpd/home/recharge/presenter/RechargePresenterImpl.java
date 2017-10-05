package com.tokopedia.tkpd.home.recharge.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeInteractor;
import com.tokopedia.tkpd.home.recharge.interactor.RechargeInteractorImpl;
import com.tokopedia.tkpd.home.recharge.view.RechargeView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Kulomady 05 on 7/13/2016.
 */
public class RechargePresenterImpl implements RechargePresenter,
        RechargeInteractor.OnGetListProduct,
        RechargeInteractor.OnGetOperatorByIdListener, RechargeInteractor.OnGetRecentNumberListener,
        RechargeInteractor.OnGetListProductForOperator, RechargeInteractor.OnGetListOperatorByIdsListener,
        RechargeInteractor.OnGetProductById, RechargeInteractor.OnGetDetailProduct {

    static final String KEY_LAST_ORDER = "RECHARGE_LAST_ORDER";
    static final String RECHARGE_CACHE_KEY = "PrimaryRechargeCache";
    private static final String RECHARGE_PHONEBOOK_CACHE_KEY = "RECHARGE_CACHE";
    private final LocalCacheHandler cacheHandlerPhoneBook;
    private final LocalCacheHandler cacheHandlerLastOrder;
    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;
    private RechargeView view;
    private RechargeInteractor dbInteractor;
    private Context context;

    public RechargePresenterImpl(Context context, RechargeView view) {
        this.view = view;
        this.dbInteractor = new RechargeInteractorImpl();
        this.context = context;
        this.cacheHandlerPhoneBook = new LocalCacheHandler(this.context, RECHARGE_PHONEBOOK_CACHE_KEY);
        this.cacheHandlerLastOrder = new LocalCacheHandler(
                this.context, RECHARGE_CACHE_KEY);
    }


    @Override
    public void fetchDataProducts() {

    }

    @Override
    public void fetchRecentNumbers(int categoryId) {
        dbInteractor.getRecentData(categoryId, this);
    }

    @Override
    public void validatePhonePrefix(String phonePrefix, int categoryId, final Boolean validatePrefix) {
        dbInteractor.getListProduct(this, phonePrefix, categoryId, validatePrefix);
    }

    @Override
    public void updateMinLenghAndOperator(String operatorId) {
        dbInteractor.getOperatorById(operatorId, this);
    }

    @Override
    public void validateWithOperator(int categoryId, String operatorId) {
        dbInteractor.getListProductDefaultOperator(this, categoryId, operatorId);
    }

    @Override
    public void getListOperatorFromCategory(int categoryId) {
        dbInteractor.getListProductForOperator(this, categoryId);
    }

    @Override
    public void validateOperatorWithoutProduct(int categoryId, String operatorId) {
        dbInteractor.getDetailProductFromOperator(this, categoryId, operatorId);
    }

    @Override
    public void getDefaultProduct(String categoryId, String operatorId, String productId) {
        dbInteractor.getProductById(this, categoryId, operatorId, productId);
    }

    @Override
    public boolean isAlreadyHavePhonebookDataOnCache(String key) {
        return null != cacheHandlerPhoneBook.getString(key);
    }

    @Override
    public boolean isAlreadyHaveLastOrderDataOnCache() {
        return null != cacheHandlerLastOrder.getString(KEY_LAST_ORDER);
    }

    @Override
    public LastOrder getLastOrderFromCache() {
        if (isAlreadyHaveLastOrderDataOnCache()) {
            String temp = cacheHandlerLastOrder.getString(KEY_LAST_ORDER);
            return CacheUtil.convertStringToModel(temp, LastOrder.class);
        } else {
            return null;
        }
    }

    @Override
    public boolean isAlreadyHaveLastOrderDataOnCacheByCategoryId(int categoryId) {
        if (isAlreadyHaveLastOrderDataOnCache()) {
            String temp = cacheHandlerLastOrder.getString(KEY_LAST_ORDER);
            try {
                LastOrder lastOrder = CacheUtil.convertStringToModel(temp, LastOrder.class);
                return (lastOrder.getData().getAttributes().getCategory_id() == categoryId);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void storeLastInstantCheckoutUsed(String categoryId, boolean checked) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    this.context, TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        cacheHandlerRecentInstantCheckoutUsed.putBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, checked
        );
        cacheHandlerRecentInstantCheckoutUsed.applyEditor();
    }

    @Override
    public boolean isRecentInstantCheckoutUsed(String categoryId) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    this.context, TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        return cacheHandlerRecentInstantCheckoutUsed.getBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, false
        );
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
    public void onSuccess(List<Product> listProduct) {
        if (!listProduct.isEmpty()) {
            processOperatorById(listProduct);
            view.renderDataProducts(listProduct);
        } else {
            view.hideFormAndImageOperator();
        }
    }

    private void processOperatorById(List<Product> products) {
        String operatorId = String.valueOf(
                products.get(0).getRelationships().getOperator().getData().getId()
        );
        dbInteractor.getOperatorById(operatorId, this);
    }

    @Override
    public void onGetRecentNumberSuccess(List<String> results) {
        view.renderDataRecent(results);
    }

    @Override
    public void onSuccessFetchOperators(List<RechargeOperatorModel> operators) {
        view.renderDataOperators(operators);
    }

    @Override
    public void onSuccessDetailProduct(List<Product> products) {
        if (!products.isEmpty()) {
            processOperatorById(products);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderDataProductsEmpty("Product List is Empty");
    }

    @Override
    public void onSuccess(RechargeOperatorModel operator) {
        view.showImageOperator(operator.image);
        view.setOperatorView(operator);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onSuccessFetchProducts(List<Product> listProduct) {
        List<Integer> operatorIds = new ArrayList<>();
        for (Product prod : listProduct) {
            if (!operatorIds.contains(prod.getRelationships().getOperator().getData().getId()))
                operatorIds.add(prod.getRelationships().getOperator().getData().getId());
        }
        dbInteractor.getOperatorListByIds(operatorIds, this);
    }

    @Override
    public void onErrorFetchProdcuts(Throwable e) {

    }

    @Override
    public void onSuccessFetchProductById(Product product) {
        view.showProductById(product);
    }
}