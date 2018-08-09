package com.tokopedia.product.manage.item.main.base.data.mapper;


import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.editproduct.EditProductServiceModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class EditProductMapper implements Func1<EditProductServiceModel, Boolean> {
    @Override
    public Boolean call(EditProductServiceModel editProductServiceModel) {
        if (editProductServiceModel.getData().getIsSuccess() == 1) {
            return true;
        } else {
            throw new RuntimeException("Edit Product Failed");
        }
    }
}
