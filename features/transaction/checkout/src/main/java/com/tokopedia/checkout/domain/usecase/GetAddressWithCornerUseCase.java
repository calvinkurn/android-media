package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addresscorner.GqlKeroWithAddressResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fajarnuha on 11/02/19.
 */
public class GetAddressWithCornerUseCase extends GraphqlUseCase {

    private Context context;

    public GetAddressWithCornerUseCase(Context context) {
        this.context = context;
    }

    @Override
    public void execute(RequestParams requestParams, Subscriber<GraphqlResponse> subscriber) {
        clearRequest();
        addRequest(getRequest());
        super.execute(requestParams, subscriber);
    }

    @Override
    public Observable<GraphqlResponse> getExecuteObservable(RequestParams requestParams) {
        clearRequest();
        addRequest(getRequest());
        return super.getExecuteObservable(requestParams).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private GraphqlRequest getRequest() {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.address_with_corner);
        return new GraphqlRequest(query, GqlKeroWithAddressResponse.class);
    }
}
