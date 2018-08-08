package com.tokopedia.product.manage.item.main.base.data.mapper;

import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.product.manage.item.main.base.domain.model.ImageProcessDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadImageMapper implements Func1<ResultUploadImage, ImageProcessDomainModel> {

    @Override
    public ImageProcessDomainModel call(ResultUploadImage resultUploadImage) {
        ImageProcessDomainModel domainModel = new ImageProcessDomainModel();
        domainModel.setUrl(resultUploadImage.getFilePath());
        domainModel.setPicObj(resultUploadImage.getPicObj());
        return domainModel;
    }
}
