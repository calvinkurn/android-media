package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.view.utils.PagingHandler;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.data.repository.PeopleAddressRepositoryImpl;

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
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    PeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleService peopleService, AddressModelMapper addressModelMapper) {
        return new PeopleAddressRepositoryImpl(peopleService, addressModelMapper);
    }

    @Provides
    PeopleAddressRepository providePeopleAddressRepository(PeopleAddressRepositoryImpl peopleAddressRepositoryImpl) {
        return peopleAddressRepositoryImpl;
    }

}