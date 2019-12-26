
package com.tokopedia.profile.following_list.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileKol {

    @SerializedName("__typename")
    @Expose
    public String typename;
    @SerializedName("following")
    @Expose
    public int following;
    @SerializedName("followers")
    @Expose
    public int followers;
    @SerializedName("followed")
    @Expose
    public boolean followed;
    @SerializedName("iskol")
    @Expose
    public boolean iskol;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("info")
    @Expose
    public String info;
    @SerializedName("bio")
    @Expose
    public String bio;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("photo")
    @Expose
    public String photo;

}
