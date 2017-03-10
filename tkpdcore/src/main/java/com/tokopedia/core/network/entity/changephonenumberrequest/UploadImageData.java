package com.tokopedia.core.network.entity.changephonenumberrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/10/17.
 */

public class UploadImageData {

    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("pic_src")
    @Expose
    private String picSrc;

    /**
     * @return The picObj
     */
    public String getPicObj() {
        return picObj;
    }

    /**
     * @param picObj The pic_obj
     */
    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    /**
     * @return The picSrc
     */
    public String getPicSrc() {
        return picSrc;
    }

    /**
     * @param picSrc The pic_src
     */
    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

}
