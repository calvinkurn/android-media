package com.tokopedia.product.manage.list.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.product.manage.list.view.model.ProductManageSortModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public interface ProductManageSortView extends CustomerView {
    void onSuccessGetListSort(List<ProductManageSortModel> productManageSortModels);
}
