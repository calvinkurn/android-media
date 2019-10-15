package com.tokopedia.affiliate.feature.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetCuratedProductSortUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetCuratedProductListUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateCuratedProductPresenter;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.travelcalendar.data.TravelCalendarHolidayRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by yfsx on 13/09/18.
 */
@Module
public class DashboardModule {

    @DashboardScope
    @Provides
    AffiliateCuratedProductPresenter provideAffiliateProductBoughtPresenter(@ApplicationContext Context context) {
        return new AffiliateCuratedProductPresenter(
                new GetCuratedProductListUseCase(context),
                new GetCuratedProductSortUseCase(context));
    }

    @DashboardScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @DashboardScope
    @Provides
    GraphqlRepository provideGraphQlRepository(@ApplicationContext Context context) {
        GraphqlClient.init(context);
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @Provides
    @DashboardScope
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

    @DashboardScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @DashboardScope
    @Provides
    @Named("travel_calendar_holiday_query")
    String providerTravelCalendarHolidayQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_travel_calendar_holiday);
    }

    @DashboardScope
    @Provides
    TravelCalendarHolidayRepository provideTravelCalendarHolidayRepository(CacheManager cacheManager) {
        return new TravelCalendarHolidayRepository(cacheManager);
    }

    @DashboardScope
    @Provides
    CacheManager provideCacheManager(@ApplicationContext Context context) {
        return new PersistentCacheManager(context);
    }
}