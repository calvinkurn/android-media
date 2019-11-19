package com.tokopedia.product.manage.list.view.listener;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse;
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel;
import com.tokopedia.topads.common.data.model.DataDeposit;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManageView extends CustomerView {

    void onLoadListEmpty();

    void onSuccessGetProductList(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage);

    void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore, String shopDomain);

    void onErrorEditPrice(Throwable t, String productId, String price, String currencyId, String currencyText);

    void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText);

    void onErrorSetCashback(Throwable t, String productId, int cashback);

    void onSuccessSetCashback(String productId, int cashback);

    void onErrorMultipleDeleteProduct(Throwable e, ProductUpdateV3SuccessFailedResponse listOfResponse);

    void onSuccessMultipleDeleteProduct();

    void showLoadingProgress();

    void hideLoadingProgress();

    void onErrorGetFreeClaim(Throwable throwable);

    void onSuccessGetFreeClaim(DataDeposit dataDeposit);

    void onSuccessGetPopUp(boolean isShowPopup, String productId);

    void onErrorGetPopUp(Throwable e);

    void onSuccessBulkUpdateProduct(ProductUpdateV3SuccessFailedResponse listOfResponse);

    void onErrorBulkUpdateProduct(Throwable e);
}
