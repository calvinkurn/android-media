package com.tokopedia.affiliate.feature.createpost.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
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
import com.tokopedia.user.session.UserSessionInterface;

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
            @ImageUploaderQualifier UserSessionInterface userSession,
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
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    @CreatePostScope
    AffiliateAnalytics provideAffiliateAnalytics(@ApplicationContext Context context,
                                                 UserSessionInterface userSessionInterface) {
        return new AffiliateAnalytics((AbstractionRouter) context, userSessionInterface);
    }
}
