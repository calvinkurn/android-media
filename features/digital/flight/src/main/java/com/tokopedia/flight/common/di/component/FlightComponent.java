package com.tokopedia.flight.common.di.component;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.booking.view.fragment.FlightInsuranceWebViewFragment;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailOrderActivity;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;
import com.tokopedia.flight.searchV3.data.db.FlightComboDao;
import com.tokopedia.flight.searchV3.data.db.FlightJourneyDao;
import com.tokopedia.flight.searchV3.data.db.FlightRouteDao;
import com.tokopedia.flight.searchV3.data.db.FlightSearchRoomDb;
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

    CacheManager cacheManager();

    FlightDateUtil flightdateutlil();

    FlightSearchRoomDb flightSearchRoomDb();

    FlightComboDao flightComboDao();

    FlightJourneyDao flightJourneyDao();

    FlightRouteDao flightRouteDao();

    Resources resources();

    void inject(BaseFlightActivity baseFlightActivity);

    void inject(FlightDetailActivity flightDetailActivity);

    void inject(FlightInsuranceWebViewFragment flightInsuranceWebViewFragment);

    void inject(FlightDetailOrderFragment flightDetailOrderFragment);

    void inject(FlightDetailOrderActivity flightDetailOrderActivity);
}