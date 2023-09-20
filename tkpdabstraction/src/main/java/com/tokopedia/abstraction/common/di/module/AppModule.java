package com.tokopedia.abstraction.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.module.net.NetModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface;
import com.tokopedia.user.session.datastore.UserSessionDataStore;
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient;

import dagger.Module;
import dagger.Provides;


/**
 * @author kulomady on 1/9/17.
 */
@Module(includes = {NetModule.class})
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
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
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    public GraphqlUseCaseInterface provideGraphqlUsecase() {
        return new GraphqlUseCase();
    }

    @ApplicationScope
    @Provides
    public UserSessionDataStore provideUserSessionDataStore(@ApplicationContext Context context) {
        return UserSessionDataStoreClient.getInstance(context);
    }

//    @ApplicationScope
//    @Provides
//    public LSdkProvider provideLsdkProvider(@ApplicationContext Context context) {
//    }
}