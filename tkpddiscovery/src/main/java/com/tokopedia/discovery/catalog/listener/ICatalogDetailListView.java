package com.tokopedia.discovery.catalog.listener;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.tokopedia.discovery.catalog.model.CatalogDetailItem;
import com.tokopedia.discovery.catalog.model.CatalogDetailListLocation;
import com.tokopedia.core.product.listener.ViewListener;

import java.util.List;

/**
 * @author by alvarisi on 10/18/16.
 */

public interface ICatalogDetailListView extends ViewListener {
    void showSortingDialog();

    void showConditionDialog();

    void showLocationDialog();

    void renderListLocation(@NonNull List<CatalogDetailListLocation> locationsData);

    void renderListCatalogProduct(@NonNull List<CatalogDetailItem> catalogDetailItems);

    void renderListCatalogProductLoadMore(@NonNull List<CatalogDetailItem> catalogDetailItems);

    void renderErrorGetCatalogProduct(String message);

    void renderErrorGetCatalogProductLoadMore(String message);

    Activity getActivity();
}
