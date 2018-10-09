package com.tokopedia.topchat.chatroom.domain.pojo.rating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 6/11/18.
 */
public class SendReasonRatingPojo {
    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }
}
