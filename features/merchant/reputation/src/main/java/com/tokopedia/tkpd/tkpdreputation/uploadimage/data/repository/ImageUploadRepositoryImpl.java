package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository;

import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class ImageUploadRepositoryImpl implements ImageUploadRepository {

    ImageUploadFactory imageUploadFactory;

    public ImageUploadRepositoryImpl(ImageUploadFactory imageUploadFactory) {
        this.imageUploadFactory = imageUploadFactory;
    }

    @Override
    public Observable<GenerateHostDomain> generateHost(RequestParams parameters) {
        return imageUploadFactory
                .createCloudGenerateHostDataSource()
                .generateHost(parameters);
    }

    @Override
    public Observable<UploadImageDomain> uploadImage(String url,
                                                     Map<String, RequestBody> params,
                                                     RequestBody imageFile) {
        return imageUploadFactory
                .createCloudUploadImageDataSource()
                .uploadImage(url,
                        params,
                        imageFile);
    }
}
