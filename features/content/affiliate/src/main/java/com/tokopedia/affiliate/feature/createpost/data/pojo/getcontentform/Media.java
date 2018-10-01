
package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Media {

    @SerializedName("multiple_media")
    @Expose
    private boolean multipleMedia = true;
    @SerializedName("max_media")
    @Expose
    private int maxMedia = 5;
    @SerializedName("media")
    @Expose
    private List<Medium> media = new ArrayList<>();

    public boolean isMultipleMedia() {
        return multipleMedia;
    }

    public void setMultipleMedia(boolean multipleMedia) {
        this.multipleMedia = multipleMedia;
    }

    public int getMaxMedia() {
        return maxMedia;
    }

    public void setMaxMedia(int maxMedia) {
        this.maxMedia = maxMedia;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

}
