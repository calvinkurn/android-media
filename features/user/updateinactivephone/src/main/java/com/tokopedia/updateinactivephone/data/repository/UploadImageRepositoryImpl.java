package com.tokopedia.updateinactivephone.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.updateinactivephone.data.factory.UploadImageSourceFactory;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

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
    public Observable<UploadImageModel> uploadImage(String url, Map<String, String> params, RequestBody imageFile) {
        return uploadImageSourceFactory
                .createCloudUploadImageDataStore()
                .uploadImage(
                        url,
                        params,
                        imageFile);
    }

    @Override
    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> parameters) {
        return uploadImageSourceFactory
                .createCloudUploadHostDataStore()
                .getUploadHost(parameters);
    }

}
