package com.tokopedia.digital.common.domain.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.entity.response.RechargePushEventRecommendationResponseEntity;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author resakemal on 01/07/19.
 */

public class RechargePushEventRecommendationUseCase extends UseCase<RechargePushEventRecommendationResponseEntity> {

    private final String PARAM_CATEGORY_ID = "categoryID";
    private final String PARAM_ACTION_TYPE = "action";

    private GraphqlUseCase graphqlUseCase;
    private Context context;

    @Inject
    public RechargePushEventRecommendationUseCase(GraphqlUseCase graphqlUseCase, @ApplicationContext Context context) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
    }

    @Override
    public Observable<RechargePushEventRecommendationResponseEntity> createObservable(RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(reqParams -> {
                    String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_recharge_push_event_recommendation);

                    Map<String, Object> variableGql = reqParams.getParameters();

                    if (!TextUtils.isEmpty(query)) {
                        GraphqlRequest request = new GraphqlRequest(query, RechargePushEventRecommendationResponseEntity.class,
                                variableGql, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(request);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                })
                .map(graphqlResponse -> graphqlResponse.getData(RechargePushEventRecommendationResponseEntity.class));
    }

    public RequestParams createRequestParam(int categoryId, String actionType) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_CATEGORY_ID, categoryId);
        requestParams.putString(PARAM_ACTION_TYPE, actionType);
        return requestParams;
    }

}
