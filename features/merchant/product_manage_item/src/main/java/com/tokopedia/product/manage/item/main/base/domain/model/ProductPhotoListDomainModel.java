package com.tokopedia.product.manage.item.main.base.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/12/17.
 */
@Deprecated
public class ProductPhotoListDomainModel {
    List<ImageProductInputDomainModel> photos;
    int productDefaultPicture;
    int originalProductDefaultPicture = -1;

    public List<ImageProductInputDomainModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputDomainModel> photos) {
        this.photos = photos;
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }

    public int getOriginalProductDefaultPicture() {
        return originalProductDefaultPicture;
    }

    public void setOriginalProductDefaultPicture(int originalProductDefaultPicture) {
        this.originalProductDefaultPicture = originalProductDefaultPicture;
    }
}
