package com.tokopedia.manageaddress.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.module.net.NetModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface;
import com.tokopedia.test.application.datastore.TestUserSessionDataStore;
import com.tokopedia.user.session.datastore.UserSessionDataStore;

import dagger.Module;
import dagger.Provides;


/**
 * @author kulomady on 1/9/17.
 */
@Module(includes = {NetModule.class})
public class FakeAppModule {

    private final Context context;

    public FakeAppModule(Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    public Context provideContext() {
        return context.getApplicationContext();
    }

    @ApplicationScope
    @Provides
    public AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context);
        } else {
            return null;
        }
    }

    @ApplicationScope
    @Provides
    public CacheManager provideGlobalCacheManager(AbstractionRouter abstractionRouter) {
        return abstractionRouter.getPersistentCacheManager();
    }

    @ApplicationScope
    @Provides
    public CoroutineDispatchers provideCoroutineDispatchers() {
        return CoroutineDispatchersProvider.INSTANCE;
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    public GraphqlRepository provideGraphqlRepository() {
        return new FakeGraphqlRepository(context);
    }

    @ApplicationScope
    @Provides
    public GraphqlUseCaseInterface provideGraphqlUsecase() {
        return new FakeGraphqlUseCase(context);
    }

    @ApplicationScope
    @Provides
    public UserSessionDataStore provideUserSessionDataStore() {
        return new TestUserSessionDataStore();
    }
}