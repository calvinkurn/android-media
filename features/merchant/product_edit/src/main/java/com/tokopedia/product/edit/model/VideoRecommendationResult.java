package com.tokopedia.product.edit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 25/06/18.
 */

public class VideoRecommendationResult {

    @SerializedName("videosearch")
    @Expose
    private VideoSearch videoSearch;

    public VideoSearch getVideoSearch() {
        return videoSearch;
    }

    public void setVideoSearch(VideoSearch videoSearch) {
        this.videoSearch = videoSearch;
    }

}
