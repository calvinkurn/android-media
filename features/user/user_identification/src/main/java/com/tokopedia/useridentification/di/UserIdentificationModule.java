package com.tokopedia.useridentification.di;

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
import com.tokopedia.user_identification_common.usecase.GetApprovalStatusUseCase;
import com.tokopedia.useridentification.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.useridentification.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.useridentification.view.listener.UserIdentificationInfo;
import com.tokopedia.useridentification.view.listener.UserIdentificationUploadImage;
import com.tokopedia.useridentification.view.presenter.UserIdentificationInfoPresenter;
import com.tokopedia.useridentification.view.presenter.UserIdentificationUploadImagePresenter;
import com.tokopedia.useridentification.view.viewmodel.AttachmentImageModel;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationScope
@Module(includes = ImageUploaderModule.class)
public class UserIdentificationModule {

    @UserIdentificationScope
    @Provides
    UserIdentificationInfo.Presenter provideUserIdentificationInfoPresenter(GetApprovalStatusUseCase getApprovalStatusUseCase) {
        return new UserIdentificationInfoPresenter(getApprovalStatusUseCase);
    }

    @UserIdentificationScope
    @Provides
    Resources provideResources(@ApplicationContext Context context){
        return context.getResources();
    }

    @UserIdentificationScope
    @Provides
    public UploadImageUseCase<AttachmentImageModel> provideAttachmentImageModelUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                                                  @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                                                  @ImageUploaderQualifier Gson gson,
                                                                                                  @ImageUploaderQualifier UserSessionInterface userSession,
                                                                                                  @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel.class, imageUploaderUtils);
    }

    @UserIdentificationScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @UserIdentificationScope
    @Provides
    UserIdentificationUploadImage.Presenter provideUploadImagePresenter(UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                                        UploadIdentificationUseCase uploadIdentificationUseCase,
                                                                        RegisterIdentificationUseCase registerIdentificationUseCase,
                                                                        com.tokopedia.user.session.UserSession userSession,
                                                                        CompositeSubscription compositeSubscription) {
        return new UserIdentificationUploadImagePresenter(uploadImageUseCase,
                uploadIdentificationUseCase,
                registerIdentificationUseCase,
                userSession,
                compositeSubscription);
    }
}
