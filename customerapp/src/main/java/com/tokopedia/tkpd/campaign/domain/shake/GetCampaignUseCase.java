package com.tokopedia.tkpd.campaign.domain.shake;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.data.entity.CampaignGqlResponse;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;

import rx.Subscriber;


/**
 * @author by nisie on 04/02/19.
 */
public class GetCampaignUseCase {

    private static final String IS_AUDIO = "is_audio";
    private static final String SCREEN_NAME = "source";
    private static final String PARAM_LATITUDE = "latitude";
    private static final String PARAM_LONGITUDE = "longitude";

    private final Resources resources;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetCampaignUseCase(Resources resources,
                              GraphqlUseCase graphqlUseCase) {
        this.resources = resources;
        this.graphqlUseCase = graphqlUseCase;
    }

    public static HashMap<String, Object> generateParam(String source,
                                                        Double latitude,
                                                        Double longitude,
                                                        Boolean isAudio) {
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put(IS_AUDIO, isAudio);
        requestParams.put(SCREEN_NAME, source);
        requestParams.put(PARAM_LATITUDE, String.valueOf(latitude));
        requestParams.put(PARAM_LONGITUDE,  String.valueOf(longitude));
        return requestParams;
    }

    public void execute(Map<String, Object> requestParams, Subscriber<GraphqlResponse> subscriber) {
        String query = GraphqlHelper.loadRawString(resources, R.raw.query_get_campaign_list);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query,
                CampaignGqlResponse.class, requestParams);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }
}
