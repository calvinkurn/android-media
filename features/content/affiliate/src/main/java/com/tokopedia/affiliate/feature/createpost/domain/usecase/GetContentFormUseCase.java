package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 9/26/18.
 */
public class GetContentFormUseCase {
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_PRODUCT_ID = "productID";
    private static final String PARAM_AD_ID = "adID";
    private static final String PARAM_POST_ID = "ID";
    private static final String TYPE_AFFILIATE = "affiliate";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    GetContentFormUseCase(@ApplicationContext Context context,
                          GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(HashMap<String, Object> variables, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_af_content_form
        );
        GraphqlRequest request = new GraphqlRequest(query, ContentFormData.class, variables, false);

        String queryQouta = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_af_quota
        );
        GraphqlRequest requestQouta = new GraphqlRequest(queryQouta, CheckQuotaQuery.class, false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(requestQouta);
        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubcribe() {
        graphqlUseCase.unsubscribe();
    }

    public static HashMap<String, Object> createRequestParams(String productId, String adId) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put(PARAM_TYPE, TYPE_AFFILIATE);
        variables.put(PARAM_PRODUCT_ID, productId);
        variables.put(PARAM_AD_ID, adId);
        return variables;
    }

    public static HashMap<String, Object> createEditRequestParams(String postId) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put(PARAM_TYPE, TYPE_AFFILIATE);
        variables.put(PARAM_POST_ID, postId);
        return variables;
    }
}
