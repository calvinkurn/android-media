package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.source;

import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class CloudUploadImageDataSource {
    private final UploadImageService uploadImageService;
    private final UploadImageMapper uploadImageMapper;

    public CloudUploadImageDataSource(UploadImageService uploadImageService,
                                      UploadImageMapper uploadImageMapper) {
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
    }

    public Observable<UploadImageDomain> uploadImage(String url,
                                                     Map<String, RequestBody> params,
                                                     RequestBody imageFile) {
        return uploadImageService.getApi()
                .uploadImage(url, params,imageFile)
                .map(uploadImageMapper);
    }
}
