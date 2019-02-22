package com.tokopedia.commonpromo;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Arrays;

import rx.Observable;

public class PromoCodeAutoApplyUseCase extends UseCase<GraphqlResponse> {

    Context mContext;

    public PromoCodeAutoApplyUseCase(Context context) {
        this.mContext = context;
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams requestParams) {
        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(mContext.getResources(),
                R.raw.tp_gql_tokopoint_apply_coupon),
                null,
                requestParams.getParameters());
        return ObservableFactory.create(Arrays.asList(request), null);
    }
}
