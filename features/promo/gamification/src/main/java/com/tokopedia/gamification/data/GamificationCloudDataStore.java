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

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GamificationCloudDataStore implements GamificationDataStore {

    private static final String QUERY_KEY = "query";

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
    public Observable<CrackResultEntity> getCrackResult(String tokenIdUser, String campaignI) {
        return gamificationApi.getCrackResult(getRequestCrackEggPayload(tokenIdUser, campaignI))
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

    private String getRequestCrackEggPayload(String tokenIdUser, String campaignId) {
        return String.format(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.crack_egg_result_mutation),
                tokenIdUser, campaignId);
    }
}
