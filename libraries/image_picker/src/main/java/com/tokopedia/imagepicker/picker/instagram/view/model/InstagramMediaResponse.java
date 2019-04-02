package com.tokopedia.imagepicker.picker.instagram.view.model;

import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramMediaResponse {
    List<InstagramMediaModel> instagramMediaModels;
    String nextMaxIdPage;

    public List<InstagramMediaModel> getInstagramMediaModels() {
        return instagramMediaModels;
    }

    public void setInstagramMediaModels(List<InstagramMediaModel> instagramMediaModels) {
        this.instagramMediaModels = instagramMediaModels;
    }

    public String getNextMaxIdPage() {
        return nextMaxIdPage;
    }

    public void setNextMaxIdPage(String nextMaxIdPage) {
        this.nextMaxIdPage = nextMaxIdPage;
    }
}
