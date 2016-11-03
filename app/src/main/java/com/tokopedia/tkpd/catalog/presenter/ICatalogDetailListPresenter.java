package com.tokopedia.tkpd.catalog.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.tkpd.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.tkpd.catalog.model.CatalogDetailItemShop;
import com.tokopedia.tkpd.catalog.model.CatalogListWrapperData;

/**
 * @author by alvarisi on 10/17/16.
 */

public interface ICatalogDetailListPresenter {
    void fetchCatalogDetailListData(@NonNull CatalogListWrapperData catalogListWrapperData);

    void fetchCatalogDetailListDataLoadMore(@NonNull CatalogListWrapperData catalogListWrapperData);

    void goToShopPage(CatalogDetailItemShop shop);

    void goToProductDetailPage(CatalogDetailItemProduct product);
}
