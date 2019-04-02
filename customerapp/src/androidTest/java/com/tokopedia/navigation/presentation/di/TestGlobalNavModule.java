package com.tokopedia.navigation.presentation.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavRouter;
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


    private MainParentPresenter mainParentPresenter;
    private GetDrawerNotificationUseCase getDrawerNotificationUseCase;

    @Provides
    MainParentPresenter provideMainParentPresenter(GetDrawerNotificationUseCase getNotificationUseCase, UserSession userSession) {
        return mainParentPresenter == null ? mainParentPresenter = new MainParentPresenter(getNotificationUseCase, userSession) : mainParentPresenter;
    }

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
    Fragment provideFragmentOne(@ApplicationContext Context context) {
        return EmptyFragment.newInstance(0);
    }

    @Provides
    ApplicationUpdate provideAppUpdate(@ApplicationContext Context context) {
        return ((GlobalNavRouter) context).getAppUpdate(context);
    }
}
