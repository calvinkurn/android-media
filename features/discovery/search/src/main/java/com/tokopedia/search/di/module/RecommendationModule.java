package com.tokopedia.search.di.module;

import android.content.Context;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.search.R;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.user.session.UserSessionInterface;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

@SearchScope
@Module
public class RecommendationModule {
    @Provides
    @SearchScope
    @Named("recommendationQuery")
    String provideRecommendationRawQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_recommendation_widget);
    }

    @Provides
    @SearchScope
    GetRecommendationUseCase provideGetRecommendationUseCase(@Named("recommendationQuery") String recomQuery,
                                                             GraphqlUseCase graphqlUseCase,
                                                             UserSessionInterface userSessionInterface) {
        return new GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface);
    }
}
