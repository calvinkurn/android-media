package com.tokopedia.digital.nostylecategory.digitalcategory.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.digital.R;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * Created by Rizky on 30/08/18.
 */
public class DigitalCategoryNoStyleUseCase {

    private final Context context;
    private GraphqlUseCase graphqlUseCase;

    public DigitalCategoryNoStyleUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        GraphqlClient.init(context);
    }

    private GraphqlRequest getRequest(RequestParams requestParams) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_agent_digital_category), ResponseAgentDigitalCategory.class, requestParams.getParameters(), false);
    }

    public RequestParams createRequestParams(int categoryId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt("category_id", categoryId);
        return requestParams;
    }

    public void execute(RequestParams requestParams, Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.addRequest(getRequest(requestParams));
        graphqlUseCase.execute(subscriber);
    }

}
