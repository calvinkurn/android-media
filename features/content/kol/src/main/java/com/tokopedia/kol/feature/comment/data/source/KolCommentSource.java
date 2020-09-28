package com.tokopedia.kol.feature.comment.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.common.data.model.request.GraphqlRequest;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.comment.data.mapper.KolDeleteCommentMapper;
import com.tokopedia.kol.feature.comment.data.mapper.KolGetCommentMapper;
import com.tokopedia.kol.feature.comment.data.mapper.KolSendCommentMapper;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.kol.feature.comment.data.raw.GqlMutationCreateKolCommentKt.GQL_MUTATION_CREATE_KOL_COMMENT;
import static com.tokopedia.kol.feature.comment.data.raw.GqlMutationDeleteKolCommentKt.GQL_MUTATION_DELETE_KOL_COMMENT;
import static com.tokopedia.kol.feature.comment.data.raw.GqlQueryGetKolCommentKt.GQL_QUERY_GET_KOL_COMMENT;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentSource {
    private final Context context;
    private final KolApi kolApi;
    private final KolGetCommentMapper kolGetCommentMapper;
    private final KolDeleteCommentMapper kolDeleteCommentMapper;
    private final KolSendCommentMapper kolSendCommentMapper;

    @Inject
    public KolCommentSource(@ApplicationContext Context context,
                            KolApi kolApi,
                            KolGetCommentMapper kolGetCommentMapper,
                            KolDeleteCommentMapper kolDeleteCommentMapper,
                            KolSendCommentMapper kolSendCommentMapper) {
        this.context = context;
        this.kolApi = kolApi;
        this.kolGetCommentMapper = kolGetCommentMapper;
        this.kolDeleteCommentMapper = kolDeleteCommentMapper;
        this.kolSendCommentMapper = kolSendCommentMapper;
    }

    public Observable<KolComments> getComments(RequestParams requestParams) {
        return kolApi.getKolComment(getRequestPayload(requestParams, GQL_QUERY_GET_KOL_COMMENT))
                .map(kolGetCommentMapper);
    }

    public Observable<SendKolCommentDomain> sendComment(RequestParams requestParams) {
        return kolApi.sendKolComment(getRequestPayload(requestParams,
                GQL_MUTATION_CREATE_KOL_COMMENT)).map(kolSendCommentMapper);
    }

    public Observable<Boolean> deleteKolComment(RequestParams requestParams) {
        return kolApi.deleteKolComment(getRequestPayload(requestParams,
                GQL_MUTATION_DELETE_KOL_COMMENT)).map(kolDeleteCommentMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, String rawResource) {
        return new GraphqlRequest(
                rawResource,
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
