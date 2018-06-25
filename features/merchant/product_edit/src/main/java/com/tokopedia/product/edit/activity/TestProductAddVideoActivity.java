package com.tokopedia.product.edit.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.edit.R;
import com.tokopedia.product.edit.model.VideoRecommendationResult;
import com.tokopedia.product.edit.model.VideoRecommendationData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by hendry on 25/06/18.
 */

public class TestProductAddVideoActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GraphqlClient.init(getApplicationContext());
        getData();
    }

    private void getData() {
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();

        Map<String, Object> variables = new HashMap<>();
        variables.put("query", "tas");
        variables.put("size", 3);

        Type type = new TypeToken<DataResponse<VideoRecommendationResult>>() {
        }.getType();
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getResources(),
                R.raw.gql_video_recommendation), type, variables);

        GraphqlCacheStrategy graphqlCacheStrategy = new GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.val())
                .setSessionIncluded(true)
                .build();
        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy);

        graphqlUseCase.setRequest(graphqlRequest);

        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // TODO just for test, remove below
                onSuccessGetVideoRecommendation(getMockUpData());
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                //DataResponse<VideoRecommendationResult> data = objects.getData(DataResponse<VideoRecommendationResult>.class);
                // TODO just for test, change mockup to original
                onSuccessGetVideoRecommendation(getMockUpData());
            }
        });
    }


    private List<VideoRecommendationData> getMockUpData() {
        Type type = new TypeToken<VideoRecommendationResult>() {
        }.getType();
        // get json string which already cached
        String jsonCachedString = "{\"videosearch\":{\"data\":[{\"id\":\"4Y7bqswecUA\",\"title\":\"tas hp\"},{\"id\":\"1dO58V-KWmI\",\"title\":\"Tas Ransel Keren\"},{\"id\":\"zFDM0IvFd98\",\"title\":\"Tas Armany  grosir tas tajur blogspot com\"}],\"error\":null}}";
        VideoRecommendationResult videoRecommendationResult = CacheUtil.convertStringToModel(jsonCachedString, type);
        return videoRecommendationResult.getVideoSearch().getData();
    }

    private void onSuccessGetVideoRecommendation(List<VideoRecommendationData> videoRecommendationDataList) {
        Log.i("Result", videoRecommendationDataList.toString());
        //TODO update UI
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
