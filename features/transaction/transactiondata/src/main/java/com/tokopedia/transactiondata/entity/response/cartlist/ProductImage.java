package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ProductImage {
    @SerializedName("image_src")
    @Expose
    private String imageSrc = "";
    @SerializedName("image_src_200_square")
    @Expose
    private String imageSrc200Square = "";
    @SerializedName("image_src_300")
    @Expose
    private String imageSrc300 = "";
    @SerializedName("image_src_square")
    @Expose
    private String imageSrcSquare = "";

    public String getImageSrc() {
        return imageSrc;
    }

    public String getImageSrc200Square() {
        return imageSrc200Square;
    }

    public String getImageSrc300() {
        return imageSrc300;
    }

    public String getImageSrcSquare() {
        return imageSrcSquare;
    }
}
