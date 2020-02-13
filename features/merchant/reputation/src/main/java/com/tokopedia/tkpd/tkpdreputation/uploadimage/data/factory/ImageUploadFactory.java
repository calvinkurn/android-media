package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.factory;

import com.tokopedia.tkpd.tkpdreputation.network.uploadimage.GenerateHostActService;
import com.tokopedia.tkpd.tkpdreputation.network.uploadimage.UploadImageService;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.source.CloudGenerateHostDataSource;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.source.CloudUploadImageDataSource;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 9/5/17.
 */

public class ImageUploadFactory {

    private final UploadImageService uploadImageService;
    private final GenerateHostActService generateHostActService;
    private final GenerateHostMapper generateHostMapper;
    private final UploadImageMapper uploadImageMapper;
    private UserSessionInterface userSession;

    public ImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper,
                              UserSessionInterface userSession) {
        this.generateHostActService = generateHostActService;
        this.uploadImageService = uploadImageService;
        this.generateHostMapper = generateHostMapper;
        this.uploadImageMapper = uploadImageMapper;
        this.userSession = userSession;
    }

    public CloudGenerateHostDataSource createCloudGenerateHostDataSource() {
        return new CloudGenerateHostDataSource(generateHostActService,generateHostMapper,userSession);
    }

    public CloudUploadImageDataSource createCloudUploadImageDataSource() {
        return new CloudUploadImageDataSource(uploadImageService, uploadImageMapper);
    }
}
