package com.tokopedia.kolcommon.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kolcommon.R;
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain;
import com.tokopedia.kolcommon.data.pojo.FollowKolQuery;
import com.tokopedia.kolcommon.model.FollowResponseModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

public class FollowKolPostGqlUseCase extends UseCase<FollowResponseModel>{
    public static final int PARAM_FOLLOW = 1;
    public static final int PARAM_UNFOLLOW = 0;
    public static final int SUCCESS_STATUS = 1;

    public static final String KEY_USER_ID_TO_FOLLOW = "userID";
    public static final String KEY_REQUESTED_ACTION = "action";

    public static RequestParams createRequestParams(int userIdToFollow, int requestedAction) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(KEY_USER_ID_TO_FOLLOW, userIdToFollow);
        requestParams.putInt(KEY_REQUESTED_ACTION, requestedAction);
        return requestParams;
    }

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public FollowKolPostGqlUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<FollowResponseModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(
                        context.getResources(),
                        R.raw.query_follow_kol
                ),
                FollowKolQuery.class,
                requestParams.getParameters()
        );

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(new Func1<GraphqlResponse, FollowResponseModel>() {
                    @Override
                    public FollowResponseModel call(GraphqlResponse graphqlResponse) {
                        FollowKolQuery followKolQuery = graphqlResponse.getData(FollowKolQuery.class);
                        if (followKolQuery.getData() != null) {
                            FollowKolDomain followKolDomain = new FollowKolDomain(
                                    followKolQuery.getData().getData().getStatus()
                            );

                            if (followKolDomain.getStatus() == FollowKolPostGqlUseCase.SUCCESS_STATUS){
                                return new FollowResponseModel(true, "");
                            } else {
                                return new FollowResponseModel(false, "");
                            }
                        } else {
                            return new FollowResponseModel(false, new Throwable().getMessage());
                        }
                    }
                });
    }
}
