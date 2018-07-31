package com.tokopedia.navigation.presentation.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by meta on 25/07/18.
 */

@Module
public class GlobalNavModule {

    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetDrawerNotificationUseCase provideGetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase) {
        return new GetDrawerNotificationUseCase(graphqlUseCase, new NotificationMapper());
    }
}
