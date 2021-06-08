package com.tokopedia.flight.common.di.component;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase;
import com.tokopedia.flight.search.data.FlightRouteDao;
import com.tokopedia.flight.search.data.cache.db.FlightSearchRoomDb;
import com.tokopedia.flight.search.data.cache.db.dao.FlightComboDao;
import com.tokopedia.flight.search.data.cache.db.dao.FlightJourneyDao;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@FlightScope
@Component(modules = FlightModule.class, dependencies = BaseAppComponent.class)
public interface FlightComponent {
    @ApplicationContext
    Context context();

    @FlightQualifier
    Retrofit getFlightRetrofit();

    FlightApi flightApi();

    FlightRepository flightRepository();

    Gson gson();

    UserSessionInterface userSessionInterface();

    FlightDateUtil flightdateutlil();

    FlightSearchRoomDb flightSearchRoomDb();

    FlightJourneyDao flightJourneyNewDao();

    FlightRouteDao flightRouteNewDao();

    FlightComboDao flightComboNewDao();

    Resources resources();

    FlightGetOrderUseCase flightGetOrderUseCase();

    GraphqlRepository graphqlRepository();

    MultiRequestGraphqlUseCase multiRequestGraphqlUseCase();

    CoroutineDispatchers dispatcherProvider();

    void inject(BaseFlightActivity baseFlightActivity);

}