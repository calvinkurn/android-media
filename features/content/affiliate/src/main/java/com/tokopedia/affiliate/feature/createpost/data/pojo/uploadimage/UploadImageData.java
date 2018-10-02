package com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by milhamj on 10/1/18.
 */
public class UploadImageData {
    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("pic_src")
    @Expose
    private String picSrc;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("success")
    @Expose
    private String success;

    public UploadImageData() {
    }

    public String getPicObj() {
        return picObj;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public String getServerId() {
        return serverId;
    }

    public String getSuccess() {
        return success;
    }
}