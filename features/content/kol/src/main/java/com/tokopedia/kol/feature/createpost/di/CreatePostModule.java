package com.tokopedia.kol.feature.createpost.di;

import com.google.gson.Gson;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.kol.feature.createpost.view.viewmodel.AttachmentImageModel;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 25/06/18.
 */
@Module(includes = ImageUploaderModule.class)
public class CreatePostModule {

    @CreatePostScope
    @Provides
    public UploadImageUseCase<AttachmentImageModel> provideAttachmentImageModelUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                                                  @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                                                  @ImageUploaderQualifier Gson gson,
                                                                                                  @ImageUploaderQualifier UserSessionInterface userSession,
                                                                                                  @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel.class, imageUploaderUtils);
    }
}
