package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.data.repository.PeopleAddressRepositoryImpl;
import com.tokopedia.checkout.view.common.utils.PagingHandler;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module
public class PeopleAddressModule {

    @Provides
    PagingHandler providePagingHandler() {
        return new PagingHandler();
    }

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    PeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleActApi peopleActApi,
                                                                   AddressModelMapper addressModelMapper) {
        return new PeopleAddressRepositoryImpl(peopleActApi, addressModelMapper);
    }

    @Provides
    PeopleAddressRepository providePeopleAddressRepository(PeopleAddressRepositoryImpl peopleAddressRepositoryImpl) {
        return peopleAddressRepositoryImpl;
    }

}