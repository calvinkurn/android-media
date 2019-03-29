package com.tokopedia.posapp.auth.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.auth.AccountApi;

import dagger.Component;

/**
 * @author okasurya on 2/23/18.
 */

@AuthScope
@Component(modules = AuthModule.class, dependencies = AppComponent.class)
public interface AuthComponent {
    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    AccountApi accountApi();
}
