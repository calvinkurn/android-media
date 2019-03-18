package com.tokopedia.navigation.presentation.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.domain.GetBottomNavNotificationUseCase;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.domain.GetNewFeedCheckerUseCase;
import com.tokopedia.navigation.listener.CartListener;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by meta on 25/07/18.
 */

@Module
public class GlobalNavModule {
    @Provides
    MainParentPresenter provideMainParentPresenter(GetBottomNavNotificationUseCase getNotificationUseCase, UserSessionInterface userSession){
        return new MainParentPresenter(getNotificationUseCase, userSession);
    }

    @Provides
    GetBottomNavNotificationUseCase provideGetBottomNavNotificationUseCase(
            GetDrawerNotificationUseCase getDrawerNotificationUseCase,
            GetNewFeedCheckerUseCase getNewFeedCheckerUseCase) {
        return new GetBottomNavNotificationUseCase(
                getDrawerNotificationUseCase,
                getNewFeedCheckerUseCase);
    }

    @Provides
    GetNewFeedCheckerUseCase provideGetNewFeedCheckerUseCase(@ApplicationContext Context context) {
        return new GetNewFeedCheckerUseCase(context);
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

    @Provides
    @GlobalNavScope
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
