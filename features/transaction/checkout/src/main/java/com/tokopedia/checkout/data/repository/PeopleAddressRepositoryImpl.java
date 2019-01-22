package com.tokopedia.checkout.data.repository;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
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
 */

public class PeopleAddressRepositoryImpl implements PeopleAddressRepository {

    private static final int FIRST_ELEMENT = 0;

    private final PeopleActApi peopleActApi;
    private final AddressModelMapper mAddressModelMapper;

    @Inject
    public PeopleAddressRepositoryImpl(PeopleActApi peopleActApi, AddressModelMapper addressModelMapper) {
        this.peopleActApi = peopleActApi;
        this.mAddressModelMapper = addressModelMapper;
    }

    /**
     * Get an {@link Observable} which will emits a {@link List < ShipmentAddressModel >}
     *
     * @param params Parameters used to retrieve address data
     * @return List of address
     */
    @Override
    public Observable<PeopleAddressModel> getAllAddress(Map<String, String> params) {
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
                                } else {
                                    // List of address is empty, therefore result will be treated
                                    // as null data
                                }

                            } else if (response.body().isNullData()) {
                                // Response body is literally null data
                            } else {
                                // Error is occurred
                                String error = response.body().getErrorMessages().get(FIRST_ELEMENT);
                            }
                        }

                        return null;
                    }
                })
                .map(new Func1<GetPeopleAddress, PeopleAddressModel>() {
                    @Override
                    public PeopleAddressModel call(GetPeopleAddress addressModels) {
                        return mAddressModelMapper.transform(addressModels);
                    }
                });
    }

}