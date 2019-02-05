package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductImage implements Parcelable {
    private static final String TAG = ProductImage.class.getSimpleName();

    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("image_src_300")
    @Expose
    private String imageSrc300;
    @SerializedName("image_status")
    @Expose
    private Integer imageStatus;
    @SerializedName("image_description")
    @Expose
    private String imageDescription;
    @SerializedName("image_primary")
    @Expose
    private Integer imagePrimary;
    @SerializedName("image_src")
    @Expose
    private String imageSrc;

    private int variantChildId;

    public ProductImage() {
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageSrc300() {
        return imageSrc300;
    }

    public void setImageSrc300(String imageSrc300) {
        this.imageSrc300 = imageSrc300;
    }

    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public Integer getImagePrimary() {
        return imagePrimary;
    }

    public void setImagePrimary(Integer imagePrimary) {
        this.imagePrimary = imagePrimary;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getVariantChildId() {
        return variantChildId;
    }

    public void setVariantChildId(int variantChildId) {
        this.variantChildId = variantChildId;
    }

    protected ProductImage(Parcel in) {
        imageId = in.readByte() == 0x00 ? null : in.readInt();
        imageSrc300 = in.readString();
        imageStatus = in.readByte() == 0x00 ? null : in.readInt();
        imageDescription = in.readString();
        imagePrimary = in.readByte() == 0x00 ? null : in.readInt();
        imageSrc = in.readString();
        variantChildId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (imageId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(imageId);
        }
        dest.writeString(imageSrc300);
        if (imageStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(imageStatus);
        }
        dest.writeString(imageDescription);
        if (imagePrimary == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(imagePrimary);
        }
        dest.writeString(imageSrc);
        dest.writeInt(variantChildId);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductImage> CREATOR = new Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };


    public static class Builder {
        private Integer imageId;
        private String imageSrc300;
        private Integer imageStatus;
        private String imageDescription;
        private Integer imagePrimary;
        private String imageSrc;

        private Builder() {
        }

        public static Builder aProductImage() {
            return new Builder();
        }

        public Builder setImageId(Integer imageId) {
            this.imageId = imageId;
            return this;
        }

        public Builder setImageSrc300(String imageSrc300) {
            this.imageSrc300 = imageSrc300;
            return this;
        }

        public Builder setImageStatus(Integer imageStatus) {
            this.imageStatus = imageStatus;
            return this;
        }

        public Builder setImageDescription(String imageDescription) {
            this.imageDescription = imageDescription;
            return this;
        }

        public Builder setImagePrimary(Integer imagePrimary) {
            this.imagePrimary = imagePrimary;
            return this;
        }

        public Builder setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
            return this;
        }

        public Builder but() {
            return aProductImage().setImageId(imageId).setImageSrc300(imageSrc300).setImageStatus(imageStatus).setImageDescription(imageDescription).setImagePrimary(imagePrimary).setImageSrc(imageSrc);
        }

        public ProductImage build() {
            ProductImage productImage = new ProductImage();
            productImage.setImageId(imageId);
            productImage.setImageSrc300(imageSrc300);
            productImage.setImageStatus(imageStatus);
            productImage.setImageDescription(imageDescription);
            productImage.setImagePrimary(imagePrimary);
            productImage.setImageSrc(imageSrc);
            return productImage;
        }
    }
}
