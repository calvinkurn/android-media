
package com.tokopedia.core.shop.model.shopinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("og_img")
    @Expose
    private String ogImg;
    @SerializedName("logo")
    @Expose
    private String logo;

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

}
