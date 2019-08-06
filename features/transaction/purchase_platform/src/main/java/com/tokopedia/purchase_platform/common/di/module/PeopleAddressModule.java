package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.domain.mapper.AddressModelMapper;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.repository.PeopleAddressRepository;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.repository.PeopleAddressRepositoryImpl;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.domain.usecase.GetAddressWithCornerUseCase;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module
public class PeopleAddressModule {

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    PeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleActApi peopleActApi,
                                                                   GetAddressWithCornerUseCase addressWithCornerUseCase) {
        return new PeopleAddressRepositoryImpl(peopleActApi, addressWithCornerUseCase);
    }

    @Provides
    PeopleAddressRepository providePeopleAddressRepository(PeopleAddressRepositoryImpl peopleAddressRepositoryImpl) {
        return peopleAddressRepositoryImpl;
    }

}