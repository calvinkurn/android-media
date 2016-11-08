package com.tokopedia.core.catalog.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.core.catalog.model.CatalogDetailItemShop;
import com.tokopedia.core.catalog.model.CatalogListWrapperData;

/**
 * @author by alvarisi on 10/17/16.
 */

public interface ICatalogDetailListPresenter {
    void fetchCatalogDetailListData(@NonNull CatalogListWrapperData catalogListWrapperData);

    void fetchCatalogDetailListDataLoadMore(@NonNull CatalogListWrapperData catalogListWrapperData);

    void goToShopPage(CatalogDetailItemShop shop);

    void goToProductDetailPage(CatalogDetailItemProduct product);
}
