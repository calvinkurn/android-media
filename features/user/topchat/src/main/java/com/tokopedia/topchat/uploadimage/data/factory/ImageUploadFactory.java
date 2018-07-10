package com.tokopedia.topchat.uploadimage.data.factory;

import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.topchat.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.topchat.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.topchat.uploadimage.data.source.CloudGenerateHostDataSource;
import com.tokopedia.topchat.uploadimage.data.source.CloudUploadImageDataSource;

import javax.inject.Inject;


/**
 * @author by nisie on 9/5/17.
 */

public class ImageUploadFactory {

    private final UploadImageService uploadImageService;
    private final GenerateHostActService generateHostActService;
    private final GenerateHostMapper generateHostMapper;
    private final UploadImageMapper uploadImageMapper;

    @Inject
    public ImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper) {
        this.generateHostActService = generateHostActService;
        this.uploadImageService = uploadImageService;
        this.generateHostMapper = generateHostMapper;
        this.uploadImageMapper = uploadImageMapper;
    }

    public CloudGenerateHostDataSource createCloudGenerateHostDataSource() {
        return new CloudGenerateHostDataSource(generateHostActService,generateHostMapper);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataSource() {
        return new CloudUploadImageDataSource(uploadImageService, uploadImageMapper);
    }
}
