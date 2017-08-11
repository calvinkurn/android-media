package com.tokopedia.posapp.domain.model.product;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductImageDomain {
    private int imageId;
    private String imageSrc300;
    private int imageStatus;
    private String imageDescription;
    private int imagePrimary;
    private String imageSrc;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageSrc300() {
        return imageSrc300;
    }

    public void setImageSrc300(String imageSrc300) {
        this.imageSrc300 = imageSrc300;
    }

    public int getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(int imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public int getImagePrimary() {
        return imagePrimary;
    }

    public void setImagePrimary(int imagePrimary) {
        this.imagePrimary = imagePrimary;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
