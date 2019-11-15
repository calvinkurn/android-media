package com.tokopedia.loginregister.registerinitial.di;

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by nisie on 10/25/18.
 */
@Module
public class RegisterInitialModule {
    @Provides
    GraphqlRepository provideGraphQlRepository(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @RegisterInitialScope
    @Provides
    CoroutineDispatcher provideMainDispatcher(){
        return Dispatchers.getMain();
    }
}
