package com.tokopedia.digital.nostylecategory.digitalcategory.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.nostylecategory.digitalcategory.domain.usecase.DigitalCategoryNoStyleUseCase;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rizky on 30/08/18.
 */
@Module
public class DigitalCategoryNoStyleModule {

    @Provides
    @DigitalCategoryNoStyleScope
    RechargeCategoryDetailMapper provideRechargeCategoryDetailMapper() {
        return new RechargeCategoryDetailMapper();
    }

    @Provides
    @DigitalCategoryNoStyleScope
    DigitalCategoryNoStyleUseCase provideAgentDigitalCategoryUseCase(@ApplicationContext  Context context,
                                                                     GraphqlUseCase graphqlUseCase) {
        graphqlUseCase.setCacheStrategy(new GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(900)
                .build());
        return new DigitalCategoryNoStyleUseCase(context, graphqlUseCase);
    }

    @Provides
    @DigitalCategoryNoStyleScope
    DigitalAnalytics provideDigitalAnalytics(AbstractionRouter abstractionRouter, @ApplicationContext Context context) {
        return new DigitalAnalytics(abstractionRouter.getAnalyticTracker(), context);
    }

}
