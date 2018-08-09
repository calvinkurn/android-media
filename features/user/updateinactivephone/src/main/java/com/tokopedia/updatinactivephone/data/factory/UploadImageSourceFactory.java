package com.tokopedia.updatinactivephone.data.factory;


import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.data.source.CloudUploadImageDataSource;

import javax.inject.Inject;

public class UploadImageSourceFactory {

    private final UploadImageService uploadImageService;
    private final UploadImageMapper uploadImageMapper;

    @Inject
    public UploadImageSourceFactory(UploadImageService uploadImageService,
                                    UploadImageMapper uploadImageMapper) {
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(uploadImageService, uploadImageMapper);
    }
}
