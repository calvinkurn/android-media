package com.tokopedia.logisticaddaddress.domain.usecase;

import com.tokopedia.logisticaddaddress.data.DataSource;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
@AddressScope
public class GetAddressUseCase extends UseCase<GetPeopleAddress> {

    public static final String PARAM_ORDER_BY = "order_by";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_QUERY = "query";

    DataSource mRepository;

    @Inject
    public GetAddressUseCase(DataSource mRepository) {
        this.mRepository = mRepository;
    }

    @Override
    public Observable<GetPeopleAddress> createObservable(RequestParams requestParams) {
        return mRepository.getAddress(requestParams);
    }

    public RequestParams getAddressParam(int page, int sortId, String query) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PAGE, page);
        params.putInt(PARAM_ORDER_BY, sortId);
        params.putString(PARAM_QUERY, query);
        return params;
    }

}
