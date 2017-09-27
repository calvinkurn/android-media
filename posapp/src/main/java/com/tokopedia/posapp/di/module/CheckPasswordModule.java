package com.tokopedia.posapp.di.module;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.di.scope.CheckPasswordScope;
import com.tokopedia.posapp.domain.usecase.CheckPasswordUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/27/17.
 */

@Module
public class CheckPasswordModule {
    @CheckPasswordScope
    @Provides
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @CheckPasswordScope
    @Provides
    ProfileMapper provideProfileMapper() {
        return new ProfileMapper();
    }

    @CheckPasswordScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @CheckPasswordScope
    @Provides
    AnalyticsCacheHandler provideAnalyticsCacheHandler() {
        return new AnalyticsCacheHandler();
    }

    @CheckPasswordScope
    @Provides
    ProfileSourceFactory provideProfileSourceFactory(@ApplicationContext Context context,
                                                     PeopleService peopleService,
                                                     ProfileMapper profileMapper,
                                                     GlobalCacheManager globalCacheManager,
                                                     AnalyticsCacheHandler analyticsCacheHandler,
                                                     SessionHandler sessionHandler) {
        return new ProfileSourceFactory(context, peopleService, profileMapper,
                globalCacheManager, analyticsCacheHandler, sessionHandler);
    }

    @CheckPasswordScope
    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @CheckPasswordScope
    @Provides
    ProfileUseCase provideProfileUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         ProfileRepository profileRepository) {
        return new ProfileUseCase(threadExecutor, postExecutionThread, profileRepository);
    }

    @CheckPasswordScope
    @Provides
    AccountsService provideAccountsService() {
        Bundle bundle = new Bundle();
        bundle.getString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.ACCOUNTS);
        bundle.putBoolean(AccountsService.USING_HMAC, true);

        return new AccountsService(bundle);
    }

    @CheckPasswordScope
    @Provides
    CheckPasswordUseCase provideCheckPasswordUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     AccountsService accountsService) {
        return new CheckPasswordUseCase(threadExecutor, postExecutionThread, accountsService);
    }
}
