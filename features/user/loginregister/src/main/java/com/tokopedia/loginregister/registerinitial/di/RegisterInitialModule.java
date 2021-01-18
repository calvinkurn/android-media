package com.tokopedia.loginregister.registerinitial.di;

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.loginregister.common.DispatcherProvider;

import org.jetbrains.annotations.NotNull;

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
    MultiRequestGraphqlUseCase provideMultiRequestGraphql(){
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @RegisterInitialScope
    @Provides
    CoroutineDispatcher provideMainDispatcher(){
        return Dispatchers.getMain();
    }

    @RegisterInitialScope
    @Provides
    DispatcherProvider provideDispatcherProvider() {
        return new DispatcherProvider() {
            @NotNull
            @Override
            public CoroutineDispatcher io() {
                return Dispatchers.getIO();
            }

            @NotNull
            @Override
            public CoroutineDispatcher ui() {
                return Dispatchers.getMain();
            }
        };
    }
}
