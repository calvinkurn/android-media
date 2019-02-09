package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.checkout.data.mapper.AddressCornerMapper;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public class GetPeopleAddressUseCase extends UseCase<PeopleAddressModel> {

    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    private final PeopleAddressRepository peopleAddressRepository;
    private final UserSessionInterface userSessionInterface;

    public GetPeopleAddressUseCase(PeopleAddressRepository peopleAddressRepository,
                                   UserSessionInterface userSessionInterface) {
        this.peopleAddressRepository = peopleAddressRepository;
        this.userSessionInterface = userSessionInterface;

    }

    @Override
    public Observable<PeopleAddressModel> createObservable(RequestParams requestParams) {
        return peopleAddressRepository.getAllAddress(requestParams.getParamsAllValueInString()).map(new AddressCornerMapper());
    }

    /**
     * @param context
     * @param order
     * @param query
     * @param page
     * @return
     */
    public RequestParams getRequestParams(final Context context,
                                          final int order,
                                          final String query,
                                          final int page) {

        // Get people address list from api requires parameter of order, keyword, and page
        final HashMap<String, String> params = new HashMap<String, String>() {{
            put(PARAM_ORDER_BY, String.valueOf(order));
            put(PARAM_QUERY, query);
            put(PARAM_PAGE, String.valueOf(page));
        }};

        // Create network auth params from plain params using auth util generator,
        // which will retrieve another params such as device id, os type and timestamp
        final HashMap<String, Object> authParams = new HashMap<String, Object>() {{
            putAll(AuthUtil.generateParamsNetwork(userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), new TKPDMapParam<>()));
        }};

        // Create request params which contains the auth params
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(authParams);
        requestParams.putAllString(params);

        return requestParams;
    }

}