package com.tokopedia.kyc_centralized.di;

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
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationInfo;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationInfoPresenter;
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationUploadImagePresenter;
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase;
import com.tokopedia.kyc_centralized.view.viewmodel.AttachmentImageModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.domain.usecase.GetApprovalStatusUseCase;
import com.tokopedia.user_identification_common.domain.usecase.GetKtpStatusUseCase;
import com.tokopedia.user_identification_common.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.user_identification_common.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.user_identification_common.util.AppSchedulerProvider;

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
        return new UploadImageUseCase<AttachmentImageModel>(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel.class, imageUploaderUtils);
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
                                                                        GetKtpStatusUseCase getKtpStatusUseCase,
                                                                        UserSession userSession,
                                                                        CompositeSubscription compositeSubscription) {
        return new UserIdentificationUploadImagePresenter(uploadImageUseCase,
                uploadIdentificationUseCase,
                registerIdentificationUseCase,
                getKtpStatusUseCase,
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

    @UserIdentificationCommonScope
    @Provides
    UserIdentificationInfo.Presenter provideUserIdentificationInfoPresenter(GetUserProjectInfoUseCase getUserProjectInfoUseCase, GetApprovalStatusUseCase getApprovalStatusUseCase) {
        return new UserIdentificationInfoPresenter(getUserProjectInfoUseCase, getApprovalStatusUseCase);
    }
}
