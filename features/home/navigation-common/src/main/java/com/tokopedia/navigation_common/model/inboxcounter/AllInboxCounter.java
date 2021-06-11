package com.tokopedia.navigation_common.model.inboxcounter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllInboxCounter {

    @SerializedName("total_int")
    @Expose
    public int totalInt = 0;
    @SerializedName("notifcenter_int")
    @Expose
    public int notifcenterInt = 0;

}
