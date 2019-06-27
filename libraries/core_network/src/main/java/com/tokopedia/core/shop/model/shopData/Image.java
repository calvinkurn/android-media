
package com.tokopedia.core.shop.model.shopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Image {

    @SerializedName("logo")
    @Expose
    String logo;
    @SerializedName("og_img")
    @Expose
    String ogImg;

    /**
     * 
     * @return
     *     The logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 
     * @param logo
     *     The logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 
     * @return
     *     The ogImg
     */
    public String getOgImg() {
        return ogImg;
    }

    /**
     * 
     * @param ogImg
     *     The og_img
     */
    public void setOgImg(String ogImg) {
        this.ogImg = ogImg;
    }

}
