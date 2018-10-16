package com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 31/07/18.
 */
public class GetUserStatusResponsePojo {
    @SerializedName("data")
    GetUserStatusDataPojo data;

    public GetUserStatusDataPojo getData() {
        return data;
    }

    public void setData(GetUserStatusDataPojo data) {
        this.data = data;
    }
}
