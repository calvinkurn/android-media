package com.tokopedia.events.data.entity.response;

/**
 * Created by ashwanityagi on 15/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponseEntity {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items")
    @Expose
    private List<ItemResponseEntity> items = null;

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

    public List<ItemResponseEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemResponseEntity> items) {
        this.items = items;
    }

}