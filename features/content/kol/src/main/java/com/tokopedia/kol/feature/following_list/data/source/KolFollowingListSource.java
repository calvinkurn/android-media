package com.tokopedia.kol.feature.following_list.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.following_list.data.mapper.KolFollowingListMapper;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 11/2/17.
 */

public class KolFollowingListSource {
    private final Context context;
    private final KolApi kolApi;
    private final KolFollowingListMapper kolFollowingListMapper;

    @Inject
    public KolFollowingListSource(@ApplicationContext Context context,
                                  KolApi kolApi,
                                  KolFollowingListMapper kolFollowingListMapper) {
        this.context = context;
        this.kolApi = kolApi;
        this.kolFollowingListMapper = kolFollowingListMapper;
    }

    public Observable<KolFollowingResultDomain> getFollowingList(RequestParams requestParams) {
        return kolApi
                .getKolFollowingList(
                        getRequestPayload(requestParams, R.raw.query_get_kol_following_list))
                .map(kolFollowingListMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, int rawResourceId) {
        return new GraphqlRequest(
                loadRawString(context.getResources(), rawResourceId),
                requestParams.getParameters()
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
