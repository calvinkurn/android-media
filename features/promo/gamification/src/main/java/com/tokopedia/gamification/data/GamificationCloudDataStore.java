package com.tokopedia.gamification.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity;
import com.tokopedia.gamification.data.entity.ResponseTokenTokopointEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class GamificationCloudDataStore implements GamificationDataStore {

    private Context context;
    private GamificationApi gamificationApi;

    public GamificationCloudDataStore(Context context, GamificationApi gamificationApi) {
        this.context = context;
        this.gamificationApi = gamificationApi;
    }

    @Override
    public Observable<TokenDataEntity> getTokenTokopoints() {
        return gamificationApi.getTokenTokopoints(getRequestTokenTokopointsPayload())
                .map(new Func1<DataResponse<ResponseTokenTokopointEntity>, TokenDataEntity>() {
                    @Override
                    public TokenDataEntity call(DataResponse<ResponseTokenTokopointEntity> responseTokenTokopointEntityDataResponse) {
                        return responseTokenTokopointEntityDataResponse.getData().getTokopointsToken();
                    }
                });
    }

    @Override
    public Observable<CrackResultEntity> getCrackResult(String tokenIdUser, String campaignI) {
        return gamificationApi.getCrackResult(getRequestCrackEggPayload(tokenIdUser, campaignI))
                .map(new Func1<DataResponse<ResponseCrackResultEntity>, CrackResultEntity>() {
                    @Override
                    public CrackResultEntity call(DataResponse<ResponseCrackResultEntity> responseCrackResultEntityDataResponse) {
                        return responseCrackResultEntityDataResponse.getData().getCrackResultEntity();
                    }
                });
    }

    private String getRequestTokenTokopointsPayload() {
        return String.format(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.token_tokopoint_query)
        );
    }

    private String getRequestCrackEggPayload(String tokenIdUser, String campaignId) {
        return String.format(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.crack_egg_result_mutation),
                tokenIdUser, campaignId);
    }
}
