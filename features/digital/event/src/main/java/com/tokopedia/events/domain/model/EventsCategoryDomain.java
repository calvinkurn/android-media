package com.tokopedia.events.domain.model;

/**
 * Created by ashwanityagi on 15/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsCategoryDomain {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("media_url")
    @Expose
    private String mediaURL;
    @SerializedName("items")
    @Expose
    private List<EventsItemDomain> items = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public List<EventsItemDomain> getItems() {
        return items;
    }

    public void setItems(List<EventsItemDomain> items) {
        this.items = items;
    }

}