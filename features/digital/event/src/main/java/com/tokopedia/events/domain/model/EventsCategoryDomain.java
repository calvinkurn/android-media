package com.tokopedia.events.domain.model;

/**
 * Created by ashwanityagi on 15/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsCategoryDomain {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items")
    @Expose
    private List<EventsItemDomain> items = null;

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

    public List<EventsItemDomain> getItems() {
        return items;
    }

    public void setItems(List<EventsItemDomain> items) {
        this.items = items;
    }

}