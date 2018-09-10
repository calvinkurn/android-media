package com.tokopedia.updateinactivephone.data.source;

import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

public class CloudUploadImageDataSource {

    private final UploadImageService uploadImageService;
    private UploadImageMapper uploadImageMapper;

    public CloudUploadImageDataSource(UploadImageService uploadImageService,
                                      UploadImageMapper uploadImageMapper) {
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
    }

    public Observable<UploadImageModel> uploadImage(String url,
                                                    Map<String, String> params,
                                                    RequestBody imageFile) {
        return uploadImageService.getApi().uploadImage(
                url,
                params,
                imageFile)
                .map(uploadImageMapper);
    }
}
