package com.tokopedia.navigation.presentation.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavConstant;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.listener.CartListener;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by meta on 25/07/18.
 */

@Module
public class GlobalNavModule {
    @Provides
    MainParentPresenter provideMainParentPresenter(GetDrawerNotificationUseCase getNotificationUseCase){
        return new MainParentPresenter(getNotificationUseCase);
    }

    @Provides
    AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    GetDrawerNotificationUseCase provideGetDrawerNotificationUseCase(GraphqlUseCase graphqlUseCase, CartListener cartListener) {
        return new GetDrawerNotificationUseCase(graphqlUseCase, new NotificationMapper(), cartListener);
    }

    @Provides
    ApplicationUpdate provideAppUpdate(@ApplicationContext Context context) {
        return ((GlobalNavRouter) context).getAppUpdate(context);
    }

    @Provides
    CartListener provideCartListener(@ApplicationContext Context context) {
        return new CartListener() {
            @Override
            public void setCartCount(int count) {
                ((GlobalNavRouter) context).setCartCount(context, count);
            }

            @Override
            public int getCartCount() {
                return ((GlobalNavRouter) context).getCartCount(context);
            }
        };
    }
}
