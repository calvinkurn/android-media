package com.tokopedia.product.manage.list.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;
import com.tokopedia.topads.common.data.model.DataDeposit;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManageView extends CustomerView {

    void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem);

    void onLoadSearchError(Throwable t);

    void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage);

    void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore, String shopDomain);

    void onSuccessGetFeaturedProductList(List<String> data);

    void onErrorEditPrice(Throwable t, String productId, String price, String currencyId, String currencyText);

    void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText);

    void onErrorSetCashback(Throwable t, String productId, int cashback);

    void onSuccessSetCashback(String productId, int cashback);

    void onErrorMultipleDeleteProduct(Throwable e, List<String> productIdDeletedList, List<String> productIdFailToDeleteList);

    void onSuccessMultipleDeleteProduct();

    void showLoadingProgress();

    void hideLoadingProgress();

    void onErrorGetFreeClaim(Throwable throwable);

    void onSuccessGetFreeClaim(DataDeposit dataDeposit);

    void onSuccessGetPopUp(boolean isShowPopup, String productId);

    void onErrorGetPopUp(Throwable e);
}
