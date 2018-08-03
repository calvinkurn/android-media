package com.tokopedia.navigation.presentation.di;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.data.mapper.DrawerNotificationMapper;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.GetNotificationUseCase;
import com.tokopedia.navigation.presentation.fragment.EmptyFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by meta on 25/07/18.
 */

@Module
public class TestGlobalNavModule {

    private GetNotificationUseCase getNotificationUseCase;
    private GetDrawerNotificationUseCase getDrawerNotificationUseCase;

    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetNotificationUseCase provideGetNotificationUseCase(GraphqlUseCase graphqlUseCase) {
        return getNotificationUseCase == null ? getNotificationUseCase = mock(GetNotificationUseCase.class) : getNotificationUseCase;
    }

    @Provides
    GetDrawerNotificationUseCase provideGetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase) {
        return getDrawerNotificationUseCase == null ? getDrawerNotificationUseCase = mock(GetDrawerNotificationUseCase.class) : getDrawerNotificationUseCase;
    }

    public GetNotificationUseCase getGetNotificationUseCase() {
        return getNotificationUseCase;
    }

    public GetDrawerNotificationUseCase getGetDrawerNotificationUseCase() {
        return getDrawerNotificationUseCase;
    }

    @Named("FRAGMENT_ONE")
    Fragment provideFragmentOne(@ApplicationContext Context context){
        return EmptyFragment.newInstance(0);
    }
}
