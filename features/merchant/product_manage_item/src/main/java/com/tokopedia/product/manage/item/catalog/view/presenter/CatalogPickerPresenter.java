package com.tokopedia.product.manage.item.catalog.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.product.manage.item.catalog.view.listener.CatalogPickerView;

/**
 * @author hendry on 4/3/17.
 */

public abstract class CatalogPickerPresenter extends BaseDaggerPresenter<CatalogPickerView>{
    public abstract void fetchCatalogData(String keyword, long departmentId, int start, int rows);
}
