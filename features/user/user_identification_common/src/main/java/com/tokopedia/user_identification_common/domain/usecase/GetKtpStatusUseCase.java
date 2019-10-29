package com.tokopedia.user_identification_common.domain.usecase;
//
// Created by Yoris Prayogo on 2019-10-29.
//

import android.content.res.Resources;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user_identification_common.R;
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo;

import javax.inject.Inject;

import rx.Subscriber;

public class GetKtpStatusUseCase {

    private final GraphqlUseCase graphqlUseCase;
    private final Resources resources;

    @Inject
    public GetKtpStatusUseCase(Resources resources, GraphqlUseCase graphqlUseCase) {
        this.resources = resources;
        this.graphqlUseCase = graphqlUseCase;
    }

   public void execute(RequestParams requestParams, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(resources, R.raw
                .query_is_ktp);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                CheckKtpStatusPojo.class, requestParams.getParameters(), false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }

    private static final String IMAGE = "image";
    private static final String IDENTIFIER = "id";
    private static final String SOURCE = "src";

    public RequestParams getRequestParam(String base64Image) {
        RequestParams param = RequestParams.create();
        param.putString(IMAGE, base64Image);
        param.putString(IDENTIFIER, "");
        param.putString(SOURCE, "");
        return param;
    }

}
