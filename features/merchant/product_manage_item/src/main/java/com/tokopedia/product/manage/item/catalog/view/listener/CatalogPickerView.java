package com.tokopedia.product.manage.item.catalog.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.Catalog;

import java.util.List;

/**
 * @author hendry on 4/4/17.
 */

public interface CatalogPickerView extends CustomerView {
    void showError(Throwable e);

    void successFetchData(List<Catalog> catalogViewModelList, int maxRows);
}
