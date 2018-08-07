
package com.tokopedia.challenges.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Prize")
    @Expose
    private String prize;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Index")
    @Expose
    private Integer index;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

}
