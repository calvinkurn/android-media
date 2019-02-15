package com.tokopedia.logisticaddaddress.di;

import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.logisticaddaddress.data.DataSource;
import com.tokopedia.logisticaddaddress.data.ManageAddressRepository;
import com.tokopedia.logisticaddaddress.domain.usecase.GetAddressUseCase;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressContract;
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressPresenter;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.module.LogisticNetworkModule;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticPeopleActApiQualifier;
import com.tokopedia.logisticdata.data.module.qualifier.LogisticUserSessionQualifier;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fajar Ulin Nuha on 18/10/18.
 */
@Module(includes = LogisticNetworkModule.class)
public class ManageAddressModule {


    public ManageAddressModule() {
    }

    @Provides
    @AddressScope
    DataSource provideDataSource(
            @LogisticPeopleActApiQualifier PeopleActApi peopleActApi,
            @LogisticUserSessionQualifier UserSessionInterface userSessionInterface
    ) {
        return new ManageAddressRepository(peopleActApi, userSessionInterface);
    }


    @Provides
    @AddressScope
    GetAddressUseCase provideGetAddressUseCase(DataSource dataSource) {
        return new GetAddressUseCase(dataSource);
    }


    @Provides
    @AddressScope
    ManageAddressContract.Presenter providePresenter(GetAddressUseCase getAddressUseCase) {
        return new ManageAddressPresenter(getAddressUseCase);
    }
}
