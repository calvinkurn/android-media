package com.tokopedia.tkpd.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 30/01/19.
 */
public class LogoutPojo {
    @SerializedName("data")
    public Data data = null;
    @SerializedName("message_error")
    public List<String> message_error = new ArrayList<>();
    @SerializedName("message_status")
    public List<String> message_status = new ArrayList<>();

    public static class Data {
        @SerializedName("is_logout")
        boolean is_logout =false;
    }
}
