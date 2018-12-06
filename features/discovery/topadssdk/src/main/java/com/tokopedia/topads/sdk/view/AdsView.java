package com.tokopedia.topads.sdk.view;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;

import java.util.List;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public interface AdsView {

    void initInjector();

    void initPresenter();

    void setDisplayMode(DisplayMode displayMode);

    void displayAds(List<Item> list, int position);

    void notifyAdsErrorLoaded(int errorCode, String message);

    void notifyProductClickListener(int position, Product product);

    void notifyShopClickListener(int position, Shop shop);

    String getString(int resId);

    void doLogin();

    void notifyAdapter();

    void showSuccessAddWishlist();

    void showErrorAddWishlist();

    void showSuccessRemoveWishlist();

    void showErrorRemoveWishlist();
}
