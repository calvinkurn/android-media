package com.tokopedia.updateinactivephone.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase;
import com.tokopedia.updateinactivephone.usecase.SubmitImageUseCase;
import com.tokopedia.updateinactivephone.usecase.UploadChangePhoneNumberRequestUseCase;
import com.tokopedia.updateinactivephone.usecase.UploadImageUseCase;
import com.tokopedia.updateinactivephone.usecase.ValidateUserDataUseCase;
import com.tokpedia.updateinactivephone.di.UpdateInactivePhoneScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UpdateInactivePhoneModule {

    @UpdateInactivePhoneScope
    @Provides
    CheckPhoneNumberStatusUsecase provideCheckPhoneNumberStatusUsecase(@ApplicationContext Context context) {
        return new CheckPhoneNumberStatusUsecase(context);
    }

    @UpdateInactivePhoneScope
    @Provides
    ValidateUserDataUseCase provideValidateUserDataUtil(@ApplicationContext Context context) {
        return new ValidateUserDataUseCase(context);
    }

    @UpdateInactivePhoneScope
    @Provides
    UploadImageUseCase provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageRepositoryImpl uploadImageRepository) {
        return new UploadImageUseCase(threadExecutor, postExecutionThread, uploadImageRepository);
    }

    @UpdateInactivePhoneScope
    @Provides
    UploadChangePhoneNumberRequestUseCase provideUploadChangePhoneNumberRequestUseCase(ThreadExecutor threadExecutor,
                                                                                       PostExecutionThread postExecutionThread,
                                                                                       UploadImageUseCase uploadImageUseCase,
                                                                                       SubmitImageUseCase submitImageUseCase) {

        return new UploadChangePhoneNumberRequestUseCase(threadExecutor, postExecutionThread, uploadImageUseCase, submitImageUseCase);
    }

    @UpdateInactivePhoneScope
    @Provides
    SubmitImageUseCase provideSubmitImageUseCase(@ApplicationContext Context context) {
        return new SubmitImageUseCase(context);
    }
}
