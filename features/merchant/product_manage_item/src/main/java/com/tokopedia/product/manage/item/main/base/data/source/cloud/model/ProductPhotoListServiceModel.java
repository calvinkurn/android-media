package com.tokopedia.product.manage.item.main.base.data.source.cloud.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class ProductPhotoListServiceModel {
    List<ProductPhotoServiceModel> photosServiceModelList;
    int productDefaultPhoto;

    public List<ProductPhotoServiceModel> getPhotosServiceModelList() {
        return photosServiceModelList;
    }

    public void setPhotosServiceModelList(List<ProductPhotoServiceModel> photosServiceModelList) {
        this.photosServiceModelList = photosServiceModelList;
    }

    public int getProductDefaultPhoto() {
        return productDefaultPhoto;
    }

    public void setProductDefaultPhoto(int productDefaultPhoto) {
        this.productDefaultPhoto = productDefaultPhoto;
    }
}
