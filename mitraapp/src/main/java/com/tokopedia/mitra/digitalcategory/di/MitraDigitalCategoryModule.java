package com.tokopedia.mitra.digitalcategory.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.mitra.digitalcategory.domain.usecase.MitraDigitalCategoryUseCase;
import com.tokopedia.mitra.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rizky on 30/08/18.
 */
@Module
public class MitraDigitalCategoryModule {

    @Provides
    @MitraDigitalCategoryScope
    RechargeCategoryDetailMapper provideRechargeCategoryDetailMapper() {
        return new RechargeCategoryDetailMapper();
    }

    @Provides
    @MitraDigitalCategoryScope
    MitraDigitalCategoryUseCase provideAgentDigitalCategoryUseCase(@ApplicationContext  Context context,
                                                                   GraphqlUseCase graphqlUseCase) {
        graphqlUseCase.setCacheStrategy(new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(900)
                .build());
        return new MitraDigitalCategoryUseCase(context, graphqlUseCase);
    }

}
