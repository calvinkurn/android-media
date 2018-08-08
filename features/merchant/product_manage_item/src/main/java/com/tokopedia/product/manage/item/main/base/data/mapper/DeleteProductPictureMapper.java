package com.tokopedia.product.manage.item.data.mapper;

import com.tokopedia.product.manage.item.data.source.cloud.model.DeleteProductPictureServiceModel;
import com.tokopedia.product.manage.item.domain.model.ImageProductInputDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class DeleteProductPictureMapper implements Func1<DeleteProductPictureServiceModel, ImageProductInputDomainModel> {
    @Override
    public ImageProductInputDomainModel call(DeleteProductPictureServiceModel deleteProductPictureServiceModel) {
        return null;
    }
}
