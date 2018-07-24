package com.tokopedia.product.edit.view.presenter;

import com.tokopedia.product.edit.view.listener.ProductAddView;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductAddView {

    void onErrorLoadProduct(Throwable throwable);

}
