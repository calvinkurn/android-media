package com.tokopedia.navigation.presentation.di;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetNotificationUseCase;

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
    GetNotificationUseCase provideGetNotificationUseCase(GraphqlUseCase graphqlUseCase) {
        return new GetNotificationUseCase(graphqlUseCase, new NotificationMapper());
    }
}
