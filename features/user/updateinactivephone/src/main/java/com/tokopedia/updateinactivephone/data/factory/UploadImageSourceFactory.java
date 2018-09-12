package com.tokopedia.updateinactivephone.data.factory;


import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper;
import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.data.source.CloudGetUploadHostSource;
import com.tokopedia.updateinactivephone.data.source.CloudUploadImageDataSource;

import javax.inject.Inject;

public class UploadImageSourceFactory {

    private final UploadImageService uploadImageService;
    private final UploadImageMapper uploadImageMapper;
    private final UploadHostMapper uploadHostMapper;

    @Inject
    public UploadImageSourceFactory(UploadImageService uploadImageService,
                                    UploadImageMapper uploadImageMapper,
                                    UploadHostMapper uploadHostMapper) {
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
        this.uploadHostMapper = uploadHostMapper;
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(uploadImageService, uploadImageMapper);
    }

    public CloudGetUploadHostSource createCloudUploadHostDataStore() {
        return new CloudGetUploadHostSource(uploadImageService, uploadHostMapper);
    }

}
