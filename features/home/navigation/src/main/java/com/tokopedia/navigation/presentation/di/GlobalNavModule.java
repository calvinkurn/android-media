package com.tokopedia.navigation.presentation.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;

import javax.inject.Named;

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

    @Named("FRAGMENT_ONE")
    Fragment provideFragmentOne(@ApplicationContext Context context) {
        return ((GlobalNavRouter) context).getHomeFragment();
    }

}
