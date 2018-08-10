package com.tokopedia.navigation.presentation.di;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.presentation.fragment.EmptyFragment;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by meta on 25/07/18.
 */

@Module
public class TestGlobalNavModule {

    private AnalyticTracker analyticTracker;

    private MainParentPresenter mainParentPresenter;

    @Provides
    MainParentPresenter provideMainParentPresenter(GetDrawerNotificationUseCase getNotificationUseCase){
        return mainParentPresenter == null ? mainParentPresenter = new MainParentPresenter(getNotificationUseCase) : mainParentPresenter;
    }

    @Provides
    AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        return analyticTracker == null ? analyticTracker = mock(AnalyticTracker.class)  : analyticTracker;
    }

    private GetDrawerNotificationUseCase getDrawerNotificationUseCase;

    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetDrawerNotificationUseCase provideGetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase) {
        return getDrawerNotificationUseCase == null ? getDrawerNotificationUseCase = mock(GetDrawerNotificationUseCase.class) : getDrawerNotificationUseCase;
    }

    public GetDrawerNotificationUseCase getGetDrawerNotificationUseCase() {
        return getDrawerNotificationUseCase;
    }

    @Named("FRAGMENT_ONE")
    Fragment provideFragmentOne(@ApplicationContext Context context){
        return EmptyFragment.newInstance(0);
    }
}
