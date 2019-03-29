
package com.tokopedia.pms.proof.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("pic_src")
    @Expose
    private String picSrc;

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

}
