package com.tokopedia.privacycenter.main.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class})
public interface TestAppComponent extends BaseAppComponent {

    GraphqlUseCaseInterface fakeGraphql();

}