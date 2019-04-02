package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 1/18/18.
 */

public class ListMethodItemPojo {
    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("mode_list")
    @Expose
    private List<ModeList> modeList = null;
    @SerializedName("info_footer")
    @Expose
    private String infoFooter = "";
    @SerializedName("link_type")
    @Expose
    private int linkType = 0;

    public int getIsSuccess() {
        return isSuccess;
    }

    public List<ModeList> getModeList() {
        return modeList;
    }

    public String getInfoFooter() {
        return infoFooter;
    }

    public int getLinkType() {
        return linkType;
    }
}
