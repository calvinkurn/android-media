package com.tokopedia.product.edit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 25/06/18.
 */

public class VideoSearch {
    @SerializedName("data")
    @Expose
    private List<VideoRecommendationData> data = null;
    @SerializedName("error")
    @Expose
    private Object error;

    public List<VideoRecommendationData> getData() {
        return data;
    }

    public void setData(List<VideoRecommendationData> data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
