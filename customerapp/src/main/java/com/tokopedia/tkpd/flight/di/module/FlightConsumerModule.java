package com.tokopedia.tkpd.flight.di.module;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * @author by alvarisi on 3/27/18.
 */
@Module
public class FlightConsumerModule {
    public FlightConsumerModule() {
    }

    @Provides
    GetUserInfoUseCase provideGetUserInfoUseCase(ProfileRepository profileRepository) {
        return new GetUserInfoUseCase(profileRepository);
    }

    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @Provides
    ProfileSourceFactory provideProfileSourceFactory(@ApplicationContext Context context,
                                                     @Named(BEARER_SERVICE) AccountsService accountsService,
                                                     GetUserInfoMapper getUserInfoMapper,
                                                     EditUserInfoMapper editUserInfoMapper,
                                                     SessionHandler sessionHandler) {
        return new ProfileSourceFactory(
                context, accountsService,
                getUserInfoMapper, editUserInfoMapper,
                sessionHandler);
    }

    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context)))
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }

    @Provides
    GetUserInfoMapper provideGetUserInfoMapper() {
        return new GetUserInfoMapper();
    }

    @Provides
    EditUserInfoMapper provideEditUserInfoMapper() {
        return new EditUserInfoMapper();
    }

}
