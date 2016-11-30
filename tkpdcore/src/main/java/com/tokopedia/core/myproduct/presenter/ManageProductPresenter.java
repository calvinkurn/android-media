package com.tokopedia.core.myproduct.presenter;

import com.tokopedia.core.myproduct.model.ManageProductModel;
import com.tokopedia.core.myproduct.model.getProductList.ProductList;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created by noiz354 on 6/17/16.
 */
public interface ManageProductPresenter {
    String TAG = ManageProductPresenter.class.getSimpleName();
    String MESSAGE_TAG = TAG +" --> ";

    String CACHE_KEY = "MANAGE_PRODUCT";
    int duration = 120;

    ManageProductModel convertTo(ProductList.Product product);
    void saveCache(ManageProductPresenterImpl.CacheManageProduct cacheManageProduct) throws MalformedURLException, UnsupportedEncodingException;
    ManageProductPresenterImpl.CacheManageProduct getCache();
}
