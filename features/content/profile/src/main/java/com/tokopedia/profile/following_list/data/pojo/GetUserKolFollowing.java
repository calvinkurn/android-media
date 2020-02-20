
package com.tokopedia.profile.following_list.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserKolFollowing {

    @SerializedName("__typename")
    @Expose
    public String typename;
    @SerializedName("profileKol")
    @Expose
    public ProfileKol profileKol;
    @SerializedName("users")
    @Expose
    public List<User> users = null;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("lastCursor")
    @Expose
    public String lastCursor;

}
