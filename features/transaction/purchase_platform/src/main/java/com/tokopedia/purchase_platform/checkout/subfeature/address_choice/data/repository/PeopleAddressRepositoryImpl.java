package com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.repository;

import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.model.addresscorner.AddressCornerResponse;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.model.addresscorner.GqlKeroWithAddressResponse;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.domain.usecase.GetAddressWithCornerUseCase;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 * Refactored by fajarnuha
 */

public class PeopleAddressRepositoryImpl implements PeopleAddressRepository {

    private final PeopleActApi peopleActApi;
    private GetAddressWithCornerUseCase addressWithCornerUseCase;

    @Inject
    public PeopleAddressRepositoryImpl(PeopleActApi peopleActApi, GetAddressWithCornerUseCase addressWithCornerUseCase) {
        this.peopleActApi = peopleActApi;
        this.addressWithCornerUseCase = addressWithCornerUseCase;
    }

    @Override
    public Observable<GetPeopleAddress> getAllAddress(Map<String, String> params) {
        return peopleActApi
                .getAddress(params)
                .map(response -> {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            GetPeopleAddress peopleAddress = response.body()
                                    .convertDataObj(GetPeopleAddress.class);
                            if (!peopleAddress.getList().isEmpty()) {
                                // Successfully obtaining user's list of address
                                return peopleAddress;
                            }
                        }
                    }
                    return null;
                });
    }

    @Override
    public Observable<AddressCornerResponse> getCornerData() {
        return addressWithCornerUseCase.getObservable()
                .map(GqlKeroWithAddressResponse::getKeroAddressWithCorner);
    }

}