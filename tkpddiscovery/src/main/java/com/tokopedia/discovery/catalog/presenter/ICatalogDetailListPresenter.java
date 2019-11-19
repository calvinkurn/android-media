package com.tokopedia.discovery.catalog.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.discovery.catalog.model.CatalogDetailItemProduct;
import com.tokopedia.discovery.catalog.model.CatalogDetailItemShop;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;

/**
 * @author by alvarisi on 10/17/16.
 */

public interface ICatalogDetailListPresenter {
    void fetchCatalogDetailListData(@NonNull CatalogListWrapperData catalogListWrapperData);

    void fetchCatalogDetailListDataLoadMore(@NonNull CatalogListWrapperData catalogListWrapperData);

    void goToShopPage(CatalogDetailItemShop shop);

    void goToProductDetailPage(CatalogDetailItemProduct product);
}
