package com.tokopedia.affiliate.feature.createpost.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageResponse;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliate.feature.createpost.view.presenter.CreatePostPresenter;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = ImageUploaderModule.class)
public class CreatePostModule {

    @Provides
    @CreatePostScope
    public UploadImageUseCase<UploadImageResponse> provideUploadImageUseCase(
            @ImageUploaderQualifier UploadImageRepository uploadImageRepository,
            @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
            @ImageUploaderQualifier Gson gson,
            @ImageUploaderQualifier com.tokopedia.abstraction.common.data.model.session.UserSession userSession,
            @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(
                uploadImageRepository,
                generateHostRepository,
                gson,
                userSession,
                UploadImageResponse.class,
                imageUploaderUtils
        );
    }

    @Provides
    @CreatePostScope
    CreatePostContract.Presenter providePresenter(CreatePostPresenter createPostPresenter) {
        return createPostPresenter;
    }

    @Provides
    @CreatePostScope
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
