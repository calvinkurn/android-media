package com.tokopedia.imagepicker.picker.instagram.domain.mapper;

import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaResponse;
import com.tokopedia.imagepicker.picker.instagram.data.model.MediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramMediaMapper  {

    @Inject
    public InstagramMediaMapper() {
    }

    public InstagramMediaResponse convertToInstagramMediaModel(ResponseListMediaInstagram responseListMediaInstagram) {
        InstagramMediaResponse instagramMediaResponse = new InstagramMediaResponse();
        List<InstagramMediaModel> instagramMediaModels = new ArrayList<>();
        for(MediaInstagram mediaInstagram : responseListMediaInstagram.getData()){
            InstagramMediaModel instagramMediaModel = new InstagramMediaModel();
            instagramMediaModel.setThumbnail(mediaInstagram.getImages().getThumbnail().getUrl());
            instagramMediaModel.setImageStandardResolution(mediaInstagram.getImages().getStandardResolution().getUrl());
            instagramMediaModels.add(instagramMediaModel);
        }
        instagramMediaResponse.setInstagramMediaModels(instagramMediaModels);
        instagramMediaResponse.setNextMaxIdPage(responseListMediaInstagram.getPagination().getNextMaxId());
        return instagramMediaResponse;
    }
}
