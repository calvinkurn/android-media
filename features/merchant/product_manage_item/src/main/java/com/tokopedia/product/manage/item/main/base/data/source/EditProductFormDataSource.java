package com.tokopedia.product.manage.item.main.base.data.source;


import com.tokopedia.product.manage.item.main.base.data.source.cloud.EditProductFormCloud;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.editproductform.EditProductFormServiceModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormDataSource {
    private final EditProductFormCloud editProductFormCloud;

    @Inject
    public EditProductFormDataSource(EditProductFormCloud editProductFormCloud) {
        this.editProductFormCloud = editProductFormCloud;
    }

    public Observable<EditProductFormServiceModel> fetchEditProductForm(String productId) {
        return editProductFormCloud.fetchEditProductForm(productId);
    }


}
