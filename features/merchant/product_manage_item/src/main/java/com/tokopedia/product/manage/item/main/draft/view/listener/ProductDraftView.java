package com.tokopedia.product.manage.item.view.presenter;

import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel;
import com.tokopedia.product.manage.item.view.listener.ProductAddView;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductAddView {

    void onErrorLoadProduct(Throwable throwable);

    void onSuccessLoadProduct(ProductViewModel model);

}
