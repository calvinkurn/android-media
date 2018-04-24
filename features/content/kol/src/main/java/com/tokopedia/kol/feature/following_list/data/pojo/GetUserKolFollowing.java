
package com.example;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    public Object error;
    @SerializedName("lastCursor")
    @Expose
    public String lastCursor;

}
