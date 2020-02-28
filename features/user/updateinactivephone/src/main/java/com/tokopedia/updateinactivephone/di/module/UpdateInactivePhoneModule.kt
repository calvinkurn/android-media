package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.core.base.di.qualifier.ApplicationContext
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.scope.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase
import com.tokopedia.updateinactivephone.usecase.GetUploadHostUseCase
import com.tokopedia.updateinactivephone.usecase.SubmitImageUseCase
import com.tokopedia.updateinactivephone.usecase.UploadChangePhoneNumberRequestUseCase
import com.tokopedia.updateinactivephone.usecase.UploadImageUseCase
import com.tokopedia.updateinactivephone.usecase.ValidateUserDataUseCase

import dagger.Module
import dagger.Provides

@Module
class UpdateInactivePhoneModule {

    @UpdateInactivePhoneScope
    @Provides
    fun provideCheckPhoneNumberStatusUsecase(@ApplicationContext context: Context): CheckPhoneNumberStatusUsecase {
        return CheckPhoneNumberStatusUsecase(context)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideValidateUserDataUtil(@ApplicationContext context: Context): ValidateUserDataUseCase {
        return ValidateUserDataUseCase(context)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideUploadImageUseCase(uploadImageRepository: UploadImageRepositoryImpl): UploadImageUseCase {
        return UploadImageUseCase(uploadImageRepository)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideGetUploadHostUseCase(uploadImageRepository: UploadImageRepositoryImpl): GetUploadHostUseCase {
        return GetUploadHostUseCase(uploadImageRepository)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideUploadChangePhoneNumberRequestUseCase(uploadImageUseCase: UploadImageUseCase,
                      submitImageUseCase: SubmitImageUseCase,
                      getUploadHostUseCase: GetUploadHostUseCase): UploadChangePhoneNumberRequestUseCase {

        return UploadChangePhoneNumberRequestUseCase(uploadImageUseCase, submitImageUseCase, getUploadHostUseCase)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideSubmitImageUseCase(@ApplicationContext context: Context): SubmitImageUseCase {
        return SubmitImageUseCase(context)
    }

    companion object {

        private val WS_SERVICE = "WS_SERVICE"
    }
}
