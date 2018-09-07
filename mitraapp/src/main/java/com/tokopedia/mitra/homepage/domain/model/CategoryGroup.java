package com.tokopedia.mitra.homepage.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryGroup {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("categoryRows")
    private List<CategoryRow> rows;

    public CategoryGroup() {
    }

    public CategoryGroup(int id, String title, List<CategoryRow> rows) {
        this.id = id;
        this.title = title;
        this.rows = rows;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<CategoryRow> getRows() {
        return rows;
    }
}
