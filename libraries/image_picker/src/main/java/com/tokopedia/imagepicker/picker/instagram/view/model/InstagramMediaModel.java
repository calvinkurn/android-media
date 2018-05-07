package com.tokopedia.imagepicker.picker.instagram.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.imagepicker.picker.instagram.view.adapter.ImageInstagramAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class InstagramMediaModel implements Visitable<ImageInstagramAdapterTypeFactory> {
    private String thumbnail;

    private String imageStandardResolution;

    @Override
    public int type(ImageInstagramAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setImageStandardResolution(String imageStandardResolution) {
        this.imageStandardResolution = imageStandardResolution;
    }

    public String getImageStandardResolution() {
        return imageStandardResolution;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
