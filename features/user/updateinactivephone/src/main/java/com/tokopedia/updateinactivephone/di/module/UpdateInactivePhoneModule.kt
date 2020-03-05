package com.tokopedia.updateinactivephone.di.module

import android.app.Activity
import android.content.Context
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.scope.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase
import com.tokopedia.updateinactivephone.usecase.GetUploadHostUseCase
import com.tokopedia.updateinactivephone.usecase.SubmitImageUseCase
import com.tokopedia.updateinactivephone.usecase.UploadChangePhoneNumberRequestUseCase
import com.tokopedia.updateinactivephone.usecase.UploadImageUseCase
import com.tokopedia.updateinactivephone.usecase.ValidateUserDataUseCase
import com.tokopedia.updateinactivephone.viewmodel.presenter.ChangeInactiveFormRequestPresenter

import dagger.Module
import dagger.Provides

@Module
class UpdateInactivePhoneModule (val activity: Activity) {

    @UpdateInactivePhoneScope
    @Provides
    fun getContext(): Context = activity

    @UpdateInactivePhoneScope
    @Provides
    fun provideCheckPhoneNumberStatusUsecase(context: Context): CheckPhoneNumberStatusUsecase {
        return CheckPhoneNumberStatusUsecase(context)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideValidateUserDataUseCase(context: Context): ValidateUserDataUseCase {
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
    fun provideSubmitImageUseCase(context: Context): SubmitImageUseCase {
        return SubmitImageUseCase(context)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideChangeInactiveFormRequestPresenter(
            validateUserDataUseCase: ValidateUserDataUseCase,
            uploadChangePhoneNumberRequestUseCase: UploadChangePhoneNumberRequestUseCase
    ): ChangeInactiveFormRequestPresenter {
        return ChangeInactiveFormRequestPresenter(validateUserDataUseCase, uploadChangePhoneNumberRequestUseCase)
    }

    companion object {

        private val WS_SERVICE = "WS_SERVICE"
    }
}
