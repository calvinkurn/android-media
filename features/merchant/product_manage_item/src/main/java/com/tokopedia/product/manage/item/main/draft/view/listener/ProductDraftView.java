package com.tokopedia.product.manage.item.main.draft.view.listener;


import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductAddView {

    void onErrorLoadProduct(Throwable throwable);

    void onSuccessLoadProduct(ProductViewModel model);

}
