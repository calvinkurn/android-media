package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointStatusTierEntity {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("eggImageURL")
    private String eggImageUrl;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("nameDesc")
    private String nameDesc;

    @SerializedName("eggImageHomepageURL")
    private String eggImageHomepageURL;

    @SerializedName("backgroundImgURLMobile")
    private String backgroundImgURLMobile;

    public String getEggImageHomepageURL() {
        return eggImageHomepageURL;
    }

    public void setEggImageHomepageURL(String eggImageHomepageURL) {
        this.eggImageHomepageURL = eggImageHomepageURL;
    }

    public String getBackgroundImgURLMobile() {
        return backgroundImgURLMobile;
    }

    public void setBackgroundImgURLMobile(String backgroundImgURLMobile) {
        this.backgroundImgURLMobile = backgroundImgURLMobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEggImageUrl() {
        return eggImageUrl;
    }

    public void setEggImageUrl(String eggImageUrl) {
        this.eggImageUrl = eggImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }

    @Override
    public String toString() {
        return "TokoPointStatusTierEntity{" +
                "id=" + id +
                ", eggImageUrl='" + eggImageUrl + '\'' +
                ", name='" + name + '\'' +
                ", nameDesc='" + nameDesc + '\'' +
                '}';
    }
}
