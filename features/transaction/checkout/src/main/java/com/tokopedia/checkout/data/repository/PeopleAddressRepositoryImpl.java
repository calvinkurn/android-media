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
                });
    }

    @Override
    public Observable<AddressCornerResponse> getCornerData(Map<String, String> params) {
        return Observable.just(new Gson().fromJson("{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"config\": null,\n" +
                "  \"server_process_time\": \"\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"addr_id\": 4651717,\n" +
                "      \"receiver_name\": \"indri\",\n" +
                "      \"addr_name\": \"Alamat tanpa pp\",\n" +
                "      \"address_1\": \"jalan karet sawah nomor 221 setiabudi jaksel\",\n" +
                "      \"address_2\": \"-6.219568765727385,106.83258723467588\",\n" +
                "      \"postal_code\": \"12940\",\n" +
                "      \"province\": 13,\n" +
                "      \"city\": 175,\n" +
                "      \"district\": 2270,\n" +
                "      \"phone\": \"6281213580787\",\n" +
                "      \"province_name\": \"DKI Jakarta\",\n" +
                "      \"city_name\": \"Kota Administrasi Jakarta Selatan\",\n" +
                "      \"district_name\": \"Setiabudi\",\n" +
                "      \"status\": 2,\n" +
                "      \"country\": \"Indonesia\",\n" +
                "      \"latitude\": \"-6.219568765727385\",\n" +
                "      \"longitude\": \"106.83258723467588\",\n" +
                "      \"is_primary\": true,\n" +
                "      \"is_whitelist\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"addr_id\": 4652534,\n" +
                "      \"receiver_name\": \"indri cilegon\",\n" +
                "      \"addr_name\": \"test cilegon\",\n" +
                "      \"address_1\": \"di banten kota cilegon merak\",\n" +
                "      \"address_2\": \"-5.930155000000001,105.99685199999999\",\n" +
                "      \"postal_code\": \"42438\",\n" +
                "      \"province\": 11,\n" +
                "      \"city\": 145,\n" +
                "      \"district\": 1626,\n" +
                "      \"phone\": \"62819201920910\",\n" +
                "      \"province_name\": \"Banten\",\n" +
                "      \"city_name\": \"Kota Cilegon\",\n" +
                "      \"district_name\": \"Merak\",\n" +
                "      \"status\": 1,\n" +
                "      \"country\": \"Indonesia\",\n" +
                "      \"latitude\": \"-5.930155000000001\",\n" +
                "      \"longitude\": \"105.99685199999999\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"addr_id\": 4652465,\n" +
                "      \"receiver_name\": \"Rey\",\n" +
                "      \"addr_name\": \"Edit internal address\",\n" +
                "      \"address_1\": \"asgasgasgasgasgasgsadasdadasada\",\n" +
                "      \"address_2\": \"asgasgasgasgasgasgsadasdadasada\",\n" +
                "      \"postal_code\": \"12312\",\n" +
                "      \"province\": 2131,\n" +
                "      \"city\": 1,\n" +
                "      \"district\": 1,\n" +
                "      \"phone\": \"6281111111111\",\n" +
                "      \"district_name\": \"Kaway XVI\",\n" +
                "      \"status\": 1,\n" +
                "      \"country\": \"Indonesia\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tokopedia_corner_data\": [\n" +
                "    {\n" +
                "      \"user_fullname\": \"donudon\",\n" +
                "      \"corner_type\": 1,\n" +
                "      \"user_corner_id\": \"12345678\",\n" +
                "      \"corner_name\": \"Bina Nusantara\",\n" +
                "      \"corner_branch\": [\n" +
                "        {\n" +
                "          \"corner_id\": 2382,\n" +
                "          \"partner_id\": 1,\n" +
                "          \"corner_branch_name\": \"Binus Anggrek\",\n" +
                "          \"address\": \"Jl. K H. Syahdan No. 9 Kemanggisan – Palmerah Jakarta Barat 11480\",\n" +
                "          \"province_id\": 1,\n" +
                "          \"city_id\": 1,\n" +
                "          \"district_id\": 1,\n" +
                "          \"postcode\": \"11480\",\n" +
                "          \"province_name\": \"D.I. Aceh\",\n" +
                "          \"city_name\": \"Tangerang Selatan\",\n" +
                "          \"district_name\": \"Alam Sutera\",\n" +
                "          \"addr_desc\": \"deket jalan\",\n" +
                "          \"geoloc\": \"999,999\",\n" +
                "          \"status\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"corner_id\": 1234,\n" +
                "          \"partner_id\": 1,\n" +
                "          \"corner_branch_name\": \"Binus Mawar\",\n" +
                "          \"address\": \"Jl. Kebon Jeruk Raya No. 27 Kebon Jeruk Jakarta Barat 11530\",\n" +
                "          \"province_id\": 1,\n" +
                "          \"city_id\": 1,\n" +
                "          \"district_id\": 1,\n" +
                "          \"postcode\": \"11530\",\n" +
                "          \"province_name\": \"D.I. Aceh\",\n" +
                "          \"city_name\": \"Jakarta Barat\",\n" +
                "          \"district_name\": \"Syahdan\",\n" +
                "          \"addr_desc\": \"deket jalan\",\n" +
                "          \"geoloc\": \"999,999\",\n" +
                "          \"status\": 1\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}", AddressCornerResponse.class));
    }

}