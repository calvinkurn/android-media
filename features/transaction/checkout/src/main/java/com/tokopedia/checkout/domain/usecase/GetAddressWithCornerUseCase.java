package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addresscorner.GqlKeroWithAddressResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fajarnuha on 11/02/19.
 */
public class GetAddressWithCornerUseCase {

    private Context context;
    private GraphqlUseCase usecase;

    public GetAddressWithCornerUseCase(Context context, GraphqlUseCase usecase) {
        this.context = context;
        this.usecase = usecase;
    }

    public Observable<GqlKeroWithAddressResponse> getObservable() {
        usecase.clearCache();
        usecase.addRequest(getRequest());
        return usecase.getExecuteObservable(null)
                .map(graphqlResponse -> graphqlResponse.<GqlKeroWithAddressResponse>getData(GqlKeroWithAddressResponse.class))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private GraphqlRequest getRequest() {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.address_with_corner);
        return new GraphqlRequest(query, GqlKeroWithAddressResponse.class);
    }
}
