package com.tokopedia.core.product.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angga.Prasetiyo on 23/10/2015.
 */

@Deprecated
public class ProductImage implements Parcelable {
    private static final String TAG = ProductImage.class.getSimpleName();

    private String imgSrc;
    private String imgDescEnc;

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getImgDescEnc() {
        return imgDescEnc;
    }

    public void setImgDescEnc(String imgDescEnc) {
        this.imgDescEnc = imgDescEnc;
    }

    protected ProductImage(Parcel in) {
        imgSrc = in.readString();
        imgDescEnc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgSrc);
        dest.writeString(imgDescEnc);
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
}
