package com.tokopedia.chooseaccount.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by nisie on 10/22/18.
 */
@Module
public class ChooseAccountModule {

    @ChooseAccountScope
    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

    @Provides
    GraphqlRepository provideGraphQlRepository(){
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @ChooseAccountScope
    @Provides
    CoroutineDispatcher provideMainDispatcher(){
        return Dispatchers.getMain();
    }

    @ChooseAccountScope
    @Provides
    CoroutineDispatchers provideCoroutineDispatcher() {
        return CoroutineDispatchersProvider.INSTANCE;
    }

}
