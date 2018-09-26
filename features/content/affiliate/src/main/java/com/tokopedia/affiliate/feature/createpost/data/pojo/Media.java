
package com.tokopedia.affiliate.feature.createpost.data.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("multiple_media")
    @Expose
    private boolean multipleMedia;
    @SerializedName("max_media")
    @Expose
    private int maxMedia;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

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
