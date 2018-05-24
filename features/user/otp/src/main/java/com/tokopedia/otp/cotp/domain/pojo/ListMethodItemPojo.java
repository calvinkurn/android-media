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

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<ModeList> getModeList() {
        return modeList;
    }

    public void setModeList(List<ModeList> modeList) {
        this.modeList = modeList;
    }
}
