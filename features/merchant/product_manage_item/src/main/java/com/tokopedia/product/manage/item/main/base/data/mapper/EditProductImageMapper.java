package com.tokopedia.product.manage.item.main.base.data.mapper;

import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.editimageproduct.EditImageProductServiceModel;
import com.tokopedia.product.manage.item.main.base.domain.model.EditImageProductDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class EditProductImageMapper implements Func1<EditImageProductServiceModel, EditImageProductDomainModel> {
    @Override
    public EditImageProductDomainModel call(EditImageProductServiceModel serviceModel) {
        EditImageProductDomainModel domainModel = new EditImageProductDomainModel();
        domainModel.setPicId(serviceModel.getData().getPicId());
        return domainModel;
    }
}
