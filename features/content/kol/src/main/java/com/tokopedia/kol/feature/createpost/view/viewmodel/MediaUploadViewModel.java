package com.tokopedia.kol.feature.createpost.view.viewmodel;

/**
 * @author by yfsx on 25/06/18.
 */
public class MediaUploadViewModel {
    private String mediaPath;
    private boolean isVideo;

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
