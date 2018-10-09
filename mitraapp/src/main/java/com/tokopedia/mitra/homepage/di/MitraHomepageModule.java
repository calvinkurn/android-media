package com.tokopedia.mitra.homepage.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.mitra.homepage.domain.MitraHomepageCategoriesUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class MitraHomepageModule {
    public MitraHomepageModule() {

    }

    @Provides
    MitraHomepageCategoriesUseCase provideMitraHomepageCategoriesUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        return new MitraHomepageCategoriesUseCase(context, graphqlUseCase);
    }

    @Provides
    GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }
}
