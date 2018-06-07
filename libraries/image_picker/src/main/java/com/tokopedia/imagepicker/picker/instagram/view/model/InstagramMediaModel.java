package com.tokopedia.imagepicker.picker.instagram.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class InstagramMediaModel implements Visitable<ImageInstagramAdapterTypeFactory> {
    private String thumbnail;

    private String imageStandardResolutionUrl;
    private String id;

    private int standardWidth;
    private int standardHeight;

    private String caption;

    public InstagramMediaModel(String id, String thumbnail, String imageStandardResolutionUrl,
                               int standardWidth, int standardHeight,
                               String caption) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.imageStandardResolutionUrl = imageStandardResolutionUrl;
        this.standardWidth = standardWidth;
        this.standardHeight = standardHeight;
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public String getId() {
        return id;
    }

    public int getMinResolution() {
        return Math.min(standardHeight, standardWidth);
    }

    @Override
    public int type(ImageInstagramAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageStandardResolutionUrl() {
        return imageStandardResolutionUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
