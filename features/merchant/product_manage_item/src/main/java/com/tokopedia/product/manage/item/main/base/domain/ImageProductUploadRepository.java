package com.tokopedia.product.manage.item.main.base.domain;

import com.tokopedia.product.manage.item.main.base.domain.model.AddProductPictureDomainModel;
import com.tokopedia.product.manage.item.main.base.domain.model.ImageProcessDomainModel;
import com.tokopedia.product.manage.item.main.base.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId);

    Observable<AddProductPictureDomainModel> addProductPicture(UploadProductInputDomainModel domainModel);
}
