package com.tokopedia.navigation_common.model.inboxcounter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxCounter {

    @SerializedName("all")
    @Expose
    public AllInboxCounter all = new AllInboxCounter();

}
