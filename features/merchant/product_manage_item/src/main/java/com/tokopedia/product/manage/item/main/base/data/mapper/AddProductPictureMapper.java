package com.tokopedia.product.manage.item.main.base.data.mapper;


import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.addproductpicture.AddProductPictureResult;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.product.manage.item.main.base.domain.model.AddProductPictureDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureMapper implements Func1<AddProductPictureServiceModel, AddProductPictureDomainModel> {
    @Override
    public AddProductPictureDomainModel call(AddProductPictureServiceModel serviceModel) {
        AddProductPictureDomainModel domainModel = new AddProductPictureDomainModel();
        AddProductPictureResult result = serviceModel.getAddProductPictureResult();
        domainModel.setFileUploaded(result.getFileUploaded());
        return domainModel;
    }
}
