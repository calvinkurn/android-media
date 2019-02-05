
package com.tokopedia.core.shop.model.uploadShopLogoData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Data {

    @SerializedName("upload")
    @Expose
    Upload upload;
    @SerializedName("image")
    @Expose
    Image image;

    /**
     *
     * @return
     * The image
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return
     *     The upload
     */
    public Upload getUpload() {
        return upload;
    }

    /**
     *
     * @param upload
     *     The upload
     */
    public void setUpload(Upload upload) {
        this.upload = upload;
    }

}
