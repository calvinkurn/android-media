package com.tokopedia.privacycenter.main.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.module.AppModule;
import com.tokopedia.abstraction.common.di.module.net.NetModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;

import dagger.Module;
import dagger.Provides;

public class FakeAppModule extends AppModule {

    public FakeAppModule(Context context) {
        super(context);
    }

    @Override
    public GraphqlRepository provideGraphqlRepository() {
        return new FakeGraphqlRepository();
    }
}
