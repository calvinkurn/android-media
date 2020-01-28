package com.tokopedia.topads.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataSuggestions {

    @SerializedName("type")
    private String type;
    @SerializedName("ids")
    private List<Integer> ids;

    public DataSuggestions(String type, List<Integer> ids) {
        this.type = type;
        this.ids = ids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
