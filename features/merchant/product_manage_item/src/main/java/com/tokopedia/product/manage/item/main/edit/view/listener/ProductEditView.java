package com.tokopedia.product.manage.item.main.edit.view.listener;

import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface ProductEditView extends ProductAddView {

    void onErrorFetchEditProduct(Throwable throwable);

    void onSuccessLoadProduct(ProductViewModel model);
}
