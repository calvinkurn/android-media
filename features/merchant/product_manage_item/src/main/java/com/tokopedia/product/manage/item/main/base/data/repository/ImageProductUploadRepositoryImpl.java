package com.tokopedia.product.manage.item.main.base.data.repository;


import com.tokopedia.product.manage.item.main.base.data.mapper.AddProductPictureMapper;
import com.tokopedia.product.manage.item.main.base.data.mapper.UploadImageMapper;
import com.tokopedia.product.manage.item.main.base.data.mapper.UploadProductPictureInputMapper;
import com.tokopedia.product.manage.item.main.base.data.source.ImageProductUploadDataSource;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.product.manage.item.main.base.domain.ImageProductUploadRepository;
import com.tokopedia.product.manage.item.main.base.domain.model.AddProductPictureDomainModel;
import com.tokopedia.product.manage.item.main.base.domain.model.ImageProcessDomainModel;
import com.tokopedia.product.manage.item.main.base.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */
@Deprecated
public class ImageProductUploadRepositoryImpl implements ImageProductUploadRepository {
    private final ImageProductUploadDataSource imageProductUploadDataSource;
    private final UploadProductPictureInputMapper uploadProductPictureInputMapper;

    public ImageProductUploadRepositoryImpl(ImageProductUploadDataSource imageProductUploadDataSource,
                                            UploadProductPictureInputMapper uploadProductPictureInputMapper) {
        this.imageProductUploadDataSource = imageProductUploadDataSource;
        this.uploadProductPictureInputMapper = uploadProductPictureInputMapper;
    }

    @Override
    public Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId) {
        return imageProductUploadDataSource.uploadImage(hostUrl, serverId, imagePath, productId)
                .map(new UploadImageMapper());
    }

    @Override
    public Observable<AddProductPictureDomainModel> addProductPicture(UploadProductInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = new AddProductPictureInputServiceModel();
        uploadProductPictureInputMapper.mapProductPhoto(serviceModel, domainModel);
        return imageProductUploadDataSource.addProductPicture(serviceModel)
                .map(new AddProductPictureMapper());
    }


}
