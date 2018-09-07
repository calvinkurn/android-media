package com.tokopedia.gamification.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GamificationCloudDataStore implements GamificationDataStore {

    private static final String QUERY_KEY = "query";
    private static final String VARIABLE_KEY = "variables";
    private static final String CAMPAIGN_ID = "campaignID";
    private static final String TOKEN_USER_ID = "tokenUserID";

    private Context context;
    private GamificationApi gamificationApi;

    public GamificationCloudDataStore(Context context, GamificationApi gamificationApi) {
        this.context = context;
        this.gamificationApi = gamificationApi;
    }

    @Override
    public Observable<TokenDataEntity> getTokenTokopoints() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(QUERY_KEY, getRequestTokenTokopointsPayload());
        return gamificationApi.getTokenTokopoints(requestParams.getParamsAllValueInString())
                .map(new Func1<GraphqlResponse<ResponseTokenTokopointEntity>, TokenDataEntity>() {
                    @Override
                    public TokenDataEntity call(GraphqlResponse<ResponseTokenTokopointEntity> responseTokenTokopointEntityGraphqlResponse) {
                        return responseTokenTokopointEntityGraphqlResponse.getData().getTokopointsToken();
                    }
                });
    }

    @Override
    public Observable<CrackResultEntity> getCrackResult(int tokenIdUser, int campaignId) {
        Map<String, Object> mapContentVariable = new HashMap<>();
        mapContentVariable.put(CAMPAIGN_ID, campaignId);
        mapContentVariable.put(TOKEN_USER_ID, tokenIdUser);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(QUERY_KEY, getRequestCrackEggPayload());
        requestParams.putObject(VARIABLE_KEY, mapContentVariable);

        return gamificationApi.getCrackResult(requestParams.getParameters())
                .map(new Func1<GraphqlResponse<ResponseCrackResultEntity>, CrackResultEntity>() {
                    @Override
                    public CrackResultEntity call(GraphqlResponse<ResponseCrackResultEntity> responseCrackResultEntityGraphqlResponse) {
                        return responseCrackResultEntityGraphqlResponse.getData().getCrackResultEntity();
                    }
                });
    }

    private String getRequestTokenTokopointsPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.token_tokopoint_query);
    }

    private String getRequestCrackEggPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.crack_egg_result_mutation);
    }
}
