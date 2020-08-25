package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.kyc_centralized.KycConstants
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.util.AppDispatcherProvider
import com.tokopedia.kyc_centralized.util.DispatcherProvider
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationInfoPresenter
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationUploadImagePresenter
import com.tokopedia.kyc_centralized.view.model.AttachmentImageModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.domain.usecase.GetApprovalStatusUseCase
import com.tokopedia.user_identification_common.domain.usecase.GetKtpStatusUseCase
import com.tokopedia.user_identification_common.domain.usecase.RegisterIdentificationUseCase
import com.tokopedia.user_identification_common.domain.usecase.UploadIdentificationUseCase
import com.tokopedia.user_identification_common.util.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import rx.subscriptions.CompositeSubscription

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationCommonScope
@Module(includes = [ImageUploaderModule::class])
class UserIdentificationCommonModule {
    @UserIdentificationCommonScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideAttachmentImageModelUploadImageUseCase(@ImageUploaderQualifier uploadImageRepository: UploadImageRepository?,
                                                      @ImageUploaderQualifier generateHostRepository: GenerateHostRepository?,
                                                      @ImageUploaderQualifier gson: Gson?,
                                                      @ImageUploaderQualifier userSession: UserSessionInterface?,
                                                      @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils?): UploadImageUseCase<AttachmentImageModel> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel::class.java, imageUploaderUtils)
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUploadImagePresenter(uploadImageUseCase: UploadImageUseCase<AttachmentImageModel?>?,
                                    uploadIdentificationUseCase: UploadIdentificationUseCase?,
                                    registerIdentificationUseCase: RegisterIdentificationUseCase?,
                                    getKtpStatusUseCase: GetKtpStatusUseCase?,
                                    userSession: UserSession?,
                                    compositeSubscription: CompositeSubscription?): UserIdentificationUploadImage.Presenter {
        return UserIdentificationUploadImagePresenter(uploadImageUseCase,
                uploadIdentificationUseCase,
                registerIdentificationUseCase,
                getKtpStatusUseCase,
                userSession,
                compositeSubscription,
                AppSchedulerProvider())
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUserIdentificationInfoPresenter(getUserProjectInfoUseCase: GetUserProjectInfoUseCase?, getApprovalStatusUseCase: GetApprovalStatusUseCase?): UserIdentificationInfo.Presenter {
        return UserIdentificationInfoPresenter(getUserProjectInfoUseCase, getApprovalStatusUseCase)
    }
}