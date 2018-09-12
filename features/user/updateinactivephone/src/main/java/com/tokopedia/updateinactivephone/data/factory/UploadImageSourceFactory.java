package com.tokopedia.updateinactivephone.data.factory;


import com.tokopedia.updateinactivephone.data.mapper.UploadHostMapper;
import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper;
import com.tokopedia.updateinactivephone.data.network.service.GetUploadHostService;
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService;
import com.tokopedia.updateinactivephone.data.source.CloudGetUploadHostSource;
import com.tokopedia.updateinactivephone.data.source.CloudUploadImageDataSource;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

import javax.inject.Inject;

public class UploadImageSourceFactory {

    private final UploadImageService uploadImageService;
    private final UploadImageMapper uploadImageMapper;
    private final UploadHostMapper uploadHostMapper;
    private final GetUploadHostService getUploadHostService;

    @Inject
    public UploadImageSourceFactory(UploadImageService uploadImageService,
                                    UploadImageMapper uploadImageMapper,
                                    UploadHostMapper uploadHostMapper,
                                    GetUploadHostService getUploadHostService) {
        this.uploadImageService = uploadImageService;
        this.uploadImageMapper = uploadImageMapper;
        this.uploadHostMapper = uploadHostMapper;
        this.getUploadHostService = getUploadHostService;
    }

    public CloudUploadImageDataSource createCloudUploadImageDataStore() {
        return new CloudUploadImageDataSource(uploadImageService, uploadImageMapper);
    }

    public CloudGetUploadHostSource createCloudUploadHostDataStore() {
        return new CloudGetUploadHostSource(getUploadHostService, uploadHostMapper);
    }

}
