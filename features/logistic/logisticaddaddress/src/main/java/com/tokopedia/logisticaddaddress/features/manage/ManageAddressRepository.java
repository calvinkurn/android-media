package com.tokopedia.logisticaddaddress.features.manage;

import com.tokopedia.logisticaddaddress.di.AddressScope;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
@AddressScope
public class ManageAddressRepository implements DataSource {

    private PeopleActApi peopleActApi;
    private UserSession userSession;

    @Inject
    public ManageAddressRepository(PeopleActApi peopleActApi, UserSession userSession) {
        this.peopleActApi = peopleActApi;
        this.userSession = userSession;
    }

    @Override
    public Observable<GetPeopleAddress> getAddress(RequestParams requestParams) {
        return peopleActApi.getAddress(generateParams(requestParams))
                .map(tokopediaWsV4ResponseResponse ->
                        tokopediaWsV4ResponseResponse
                                .body()
                                .convertDataObj(GetPeopleAddress.class));
    }

    private TKPDMapParam<String, String> generateParams(RequestParams useCaseParams) {
        TKPDMapParam<String, String> tkpdMapParam = new TKPDMapParam<>();
        tkpdMapParam.putAll(useCaseParams.getParamsAllValueInString());
        return AuthUtil.generateParamsNetwork(
                userSession.getUserId(),
                userSession.getDeviceId(),
                tkpdMapParam);
    }
}
