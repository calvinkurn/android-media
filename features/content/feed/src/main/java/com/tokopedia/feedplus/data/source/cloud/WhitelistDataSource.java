package com.tokopedia.feedplus.data.source.cloud;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.WhitelistMapper;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import rx.Observable;

/**
 * @author by yfsx on 21/06/18.
 */
public class WhitelistDataSource {

    private final Context context;
    private final FeedApi feedApi;
    private final WhitelistMapper whitelistMapper;

    public WhitelistDataSource(@ApplicationContext Context context,
                               FeedApi feedApi,
                               WhitelistMapper whitelistMapper) {
        this.context = context;
        this.feedApi = feedApi;
        this.whitelistMapper = whitelistMapper;
    }

    public Observable<WhitelistDomain> getWhiteList(RequestParams requestParams) {
        return feedApi.getWhiteList(getRequestPayload(requestParams, R.raw.query_whitelist))
                .map(whitelistMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, int rawResourceId) {

        HashMap<String, Object> params = new HashMap<>();

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
