package com.tokopedia.checkout.data.repository;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addresscorner.GqlKeroWithAddressResponse;
import com.tokopedia.checkout.domain.usecase.GetAddressWithCornerUseCase;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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
                .map(new Func1<Response<TokopediaWsV4Response>, GetPeopleAddress>() {
                    @Override
                    public GetPeopleAddress call(Response<TokopediaWsV4Response> response) {
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
                    }
                });
    }

    @Override
    public Observable<AddressCornerResponse> getCornerData() {
        return addressWithCornerUseCase.getObservable()
                .map(GqlKeroWithAddressResponse::getKeroAddressWithCorner);
    }

}