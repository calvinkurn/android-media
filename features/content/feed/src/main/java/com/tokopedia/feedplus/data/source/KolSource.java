package com.tokopedia.feedplus.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tkpdfeed.feeds.FollowKol;
import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.FollowKolMapper;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class KolSource {


    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_ACTION = "action";

    private final Context context;
    private final FollowKolMapper followKolMapper;
    private final FeedApi feedApi;

    @Inject
    public KolSource(@ApplicationContext  Context context,
                     FeedApi feedApi,
                     FollowKolMapper followKolMapper) {
        this.context = context;
        this.feedApi = feedApi;
        this.followKolMapper = followKolMapper;
    }

//    public Observable<FollowKolDomain> followKolPost(RequestParams requestParams) {
//        ApolloWatcher<FollowKol.Data> apolloWatcher = apolloClient.newCall(
//                FollowKol.builder()
//                        .userID(requestParams.getInt(FollowKolPostUseCase.PARAM_USER_ID, 0))
//                        .action(requestParams.getInt(FollowKolPostUseCase.PARAM_ACTION, -1))
//                        .build()).watcher();
//
//        return RxApollo.from(apolloWatcher).map(followKolMapper);
//    }

    public Observable<FollowKolDomain> followKolPost(RequestParams requestParams) {
        return feedApi.followKol(getRequestPayload(requestParams, R.raw.query_feed))
                .map(followKolMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, int rawResourceId) {
        int userId = requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0);
        int action = requestParams.getInt(FollowKolPostUseCase.PARAM_ACTION, -1);

        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_ACTION, FollowKolPostUseCase.PARAM_ACTION);

        return new GraphqlRequest(
                loadRawString(context.getResources(), rawResourceId),
                params
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
