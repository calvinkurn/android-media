package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class TokopointTierModel {
    @SerializedName("id")
    @Expose
    private Integer id = 0;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("nameDesc")
    @Expose
    private String nameDesc = "";
    @SerializedName("eggImageHomepageURL")
    @Expose
    private String imageUrl = "";
    @SerializedName("eggImageURL")
    @Expose
    private String eggImageURL = "";

    @SerializedName("backgroundImgURLMobile")
    @Expose
    private String backgroundImgUrl = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEggImageURL() {
        return eggImageURL;
    }

    public void setEggImageURL(String eggImageURL) {
        this.eggImageURL = eggImageURL;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }
}
