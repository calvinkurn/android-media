package com.tokopedia.user_identification_common.di;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.user_identification_common.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.user_identification_common.util.AppSchedulerProvider;
import com.tokopedia.user_identification_common.view.listener.UserIdentificationUploadImage;
import com.tokopedia.user_identification_common.view.presenter.UserIdentificationUploadImagePresenter;
import com.tokopedia.user_identification_common.view.viewmodel.AttachmentImageModel;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationCommonScope
@Module(includes = ImageUploaderModule.class)
public class UserIdentificationCommonModule {

    @UserIdentificationCommonScope
    @Provides
    Resources provideResources(@ApplicationContext Context context){
        return context.getResources();
    }

    @UserIdentificationCommonScope
    @Provides
    public UploadImageUseCase<AttachmentImageModel> provideAttachmentImageModelUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                                                  @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                                                  @ImageUploaderQualifier Gson gson,
                                                                                                  @ImageUploaderQualifier UserSessionInterface userSession,
                                                                                                  @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel.class, imageUploaderUtils);
    }

    @UserIdentificationCommonScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @UserIdentificationCommonScope
    @Provides
    UserIdentificationUploadImage.Presenter provideUploadImagePresenter(UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                                        UploadIdentificationUseCase uploadIdentificationUseCase,
                                                                        RegisterIdentificationUseCase registerIdentificationUseCase,
                                                                        UserSession userSession,
                                                                        CompositeSubscription compositeSubscription) {
        return new UserIdentificationUploadImagePresenter(uploadImageUseCase,
                uploadIdentificationUseCase,
                registerIdentificationUseCase,
                userSession,
                compositeSubscription,
                new AppSchedulerProvider());
    }

    @UserIdentificationCommonScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @UserIdentificationCommonScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
