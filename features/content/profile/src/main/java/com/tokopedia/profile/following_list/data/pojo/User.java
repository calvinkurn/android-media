
package com.tokopedia.profile.following_list.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("__typename")
    @Expose
    public String typename;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("followed")
    @Expose
    public boolean followed;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("info")
    @Expose
    public String info;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("userUrl")
    @Expose
    public String userUrl;
    @SerializedName("userApplink")
    @Expose
    public String userApplink;
    @SerializedName("isInfluencer")
    @Expose
    public boolean isInfluencer;

}
