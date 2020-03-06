package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.scope.UpdateInActiveContext
import com.tokopedia.updateinactivephone.di.scope.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.usecase.*
import com.tokopedia.updateinactivephone.viewmodel.presenter.ChangeInactiveFormRequestPresenter
import dagger.Module
import dagger.Provides

@Module class UpdateInactivePhoneModule(val context: Context) {

    @Provides
    @UpdateInActiveContext
    fun provideInActivePhoneContext() = context

    @Provides
    @UpdateInactivePhoneScope
    fun provideCheckPhoneNumberStatusUsecase(@UpdateInActiveContext context: Context): CheckPhoneNumberStatusUsecase {
        return CheckPhoneNumberStatusUsecase(context)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideValidateUserDataUseCase(@UpdateInActiveContext context: Context): ValidateUserDataUseCase {
        return ValidateUserDataUseCase(context)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideUploadImageUseCase(uploadImageRepository: UploadImageRepositoryImpl): UploadImageUseCase {
        return UploadImageUseCase(uploadImageRepository)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideGetUploadHostUseCase(uploadImageRepository: UploadImageRepositoryImpl): GetUploadHostUseCase {
        return GetUploadHostUseCase(uploadImageRepository)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideUploadChangePhoneNumberRequestUseCase(uploadImageUseCase: UploadImageUseCase,
                      submitImageUseCase: SubmitImageUseCase,
                      getUploadHostUseCase: GetUploadHostUseCase): UploadChangePhoneNumberRequestUseCase {

        return UploadChangePhoneNumberRequestUseCase(uploadImageUseCase, submitImageUseCase, getUploadHostUseCase)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideSubmitImageUseCase(@UpdateInActiveContext context: Context): SubmitImageUseCase {
        return SubmitImageUseCase(context)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideChangeInactiveFormRequestPresenter(
            validateUserDataUseCase: ValidateUserDataUseCase,
            uploadChangePhoneNumberRequestUseCase: UploadChangePhoneNumberRequestUseCase
    ): ChangeInactiveFormRequestPresenter {
        return ChangeInactiveFormRequestPresenter(validateUserDataUseCase, uploadChangePhoneNumberRequestUseCase)
    }

}
