package com.tokopedia.mitra.homepage.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DynamicHomeIcon {
    @SerializedName("categoryGroup")
    List<CategoryGroup> groups;

    public DynamicHomeIcon(List<CategoryGroup> groups) {
        this.groups = groups;
    }

    public DynamicHomeIcon() {
    }

    public List<CategoryGroup> getGroups() {
        return groups;
    }
}
