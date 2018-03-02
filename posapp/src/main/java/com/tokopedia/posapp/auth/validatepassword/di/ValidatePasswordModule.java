package com.tokopedia.posapp.auth.validatepassword.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AccountsQualifier;
import com.tokopedia.posapp.auth.validatepassword.data.repository.ValidatePasswordRepository;
import com.tokopedia.posapp.auth.validatepassword.data.repository.ValidatePasswordRepositoryImpl;
import com.tokopedia.posapp.auth.validatepassword.data.factory.ValidatePasswordFactory;
import com.tokopedia.posapp.auth.validatepassword.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.auth.AccountApi;
import com.tokopedia.posapp.auth.validatepassword.domain.ValidatePasswordUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/27/17.
 */

@Module
public class ValidatePasswordModule {
    @ValidatePasswordScope
    @Provides
    ValidatePasswordRepository provideValidatePasswordRepository(ValidatePasswordFactory validatePasswordFactory) {
        return new ValidatePasswordRepositoryImpl(validatePasswordFactory);
    }
}
