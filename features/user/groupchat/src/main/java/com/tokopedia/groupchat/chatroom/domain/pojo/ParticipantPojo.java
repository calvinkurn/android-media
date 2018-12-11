package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 06/11/18
 */
public class ParticipantPojo extends BaseGroupChatPojo{

    @SerializedName("total_view")
    @Expose
    private String totalView;


    public String getTotalView() {
        return totalView;
    }
}
