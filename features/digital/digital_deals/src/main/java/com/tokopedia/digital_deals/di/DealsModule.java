package com.tokopedia.digital_deals.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.oms.di.OmsModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = OmsModule.class)
public class DealsModule {
    Context thisContext;

    public DealsModule(Context context) {
        thisContext = context;
    }

    @Provides
    @DealsScope
    ProfileSourceFactory providesProfileSourceFactory(Context context, SessionHandler sessionHandler) {
        return new ProfileSourceFactory(context,
                new PeopleService(),
                new ProfileMapper(),
                new GlobalCacheManager(),
                sessionHandler);
    }

    @Provides
    @DealsScope
    ProfileRepository providesProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @Provides
    @DealsScope
    ProfileUseCase providesProfileUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ProfileRepository profileRepository) {
        return new ProfileUseCase(
                threadExecutor, postExecutionThread, profileRepository);
    }

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    public SessionHandler provideSessionHandler(@DealsScope Context context){
        return new SessionHandler(context);
    }


    @Provides
    @DealsScope
    Context getActivityContext() {
        return thisContext;
    }
}
