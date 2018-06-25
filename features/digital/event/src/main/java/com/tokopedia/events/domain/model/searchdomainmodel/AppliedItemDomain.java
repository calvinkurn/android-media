package com.tokopedia.events.domain.model.searchdomainmodel;

import com.google.gson.annotations.SerializedName;


public class AppliedItemDomain {

    @SerializedName("id")
    private String id;


    @SerializedName("name")
    private String name;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @SerializedName("priority")
    private int priority;


    @Override
    public String toString() {
        return
                "AppliedItemDomain{" +
                        "id = '" + id + '\'' +
                        "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}