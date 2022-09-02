package com.tokopedia.loginregister.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics;
import com.tokopedia.sessioncommon.di.SessionCommonScope;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.permission.PermissionCheckerHelper;

import dagger.Component;

/**
 * @author by nisie on 10/15/18.
 */
@LoginRegisterScope
@SessionCommonScope
@Component(modules = {
        LoginRegisterModule.class,
}, dependencies = {BaseAppComponent.class})
public interface LoginRegisterComponent {

    @ApplicationContext
    Context getContext();

    UserSessionInterface provideUserSession();

    LoginRegisterAnalytics provideLoginRegisterAnalytics();

    RegisterAnalytics provideRegisterAnalytics();

    PermissionCheckerHelper providePermissionCheckerHelper();

    SeamlessLoginAnalytics provideSeamlessLoginAnalytics();

    CoroutineDispatchers provideCoroutineDispatchers();

    @ApplicationContext
    GraphqlRepository provideGqlRepository();

}