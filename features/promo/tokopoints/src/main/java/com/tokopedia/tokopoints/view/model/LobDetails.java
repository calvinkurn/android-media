package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LobDetails {

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("services")
    private List<LobItem> lobs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LobItem> getLobs() {
        return lobs;
    }

    public void setLobs(List<LobItem> lobs) {
        this.lobs = lobs;
    }

    @Override
    public String toString() {
        return "LobDetails{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}