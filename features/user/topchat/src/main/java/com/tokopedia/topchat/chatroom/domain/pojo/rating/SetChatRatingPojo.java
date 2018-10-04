package com.tokopedia.topchat.chatroom.domain.pojo.rating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author by alvinatin on 26/03/18.
 */

public class SetChatRatingPojo {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("reasons")
    @Expose
    private ArrayList<String> reasons;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<String> getReasons() {
        return reasons;
    }
}
