package com.tokopedia.updateinactivephone.data.repository;

import com.tokopedia.updateinactivephone.model.request.UploadImageModel;
import com.tokopedia.updatinactivephone.data.factory.UploadImageSourceFactory;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;

public class UploadImageRepositoryImpl implements UploadImageRepository {

    private final UploadImageSourceFactory uploadImageSourceFactory;

    @Inject
    public UploadImageRepositoryImpl(UploadImageSourceFactory uploadImageSourceFactory) {

        this.uploadImageSourceFactory = uploadImageSourceFactory;
    }


    @Override
    public Observable<UploadImageModel> uploadImage(String url, Map<String, RequestBody> params, RequestBody imageFile) {
        return uploadImageSourceFactory
                .createCloudUploadImageDataStore()
                .uploadImage(
                        url,
                        params,
                        imageFile);
    }
}
