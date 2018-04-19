package com.tokopedia.kol.feature.comment.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.comment.data.mapper.KolCommentMapper;
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 18/04/18.
 */

public class KolCommentSource {
    private final Context context;
    private final KolApi kolApi;
    private final KolCommentMapper kolCommentMapper;

    @Inject
    public KolCommentSource(@ApplicationContext Context context,
                            KolApi kolApi,
                            KolCommentMapper kolCommentMapper) {
        this.context = context;
        this.kolApi = kolApi;
        this.kolCommentMapper = kolCommentMapper;
    }

    public Observable<KolComments> getComments(RequestParams requestParams) {
        return kolApi.getKolComment(getRequestPayload(requestParams, R.raw.query_get_kol_comment))
                .map(kolCommentMapper);
    }

    public Observable<SendKolCommentDomain> sendComment(RequestParams requestParams) {
//        ApolloWatcher<CreateKolComment.Data> apolloWatcher = apolloClient.newCall(
//                CreateKolComment.builder()
//                        .comment(requestParams.getString(SendKolCommentUseCase.PARAM_COMMENT, ""))
//                        .idPost(requestParams.getInt(SendKolCommentUseCase.PARAM_ID, 0))
//                        .build()).watcher();
//        return RxApollo.from(apolloWatcher).map(kolSendCommentMapper);
        return null;
    }

    public Observable<Boolean> deleteKolComment(RequestParams requestParams) {
//        ApolloWatcher<DeleteKolComment.Data> apolloWatcher = apolloClient.newCall(
//                DeleteKolComment.builder()
//                        .idComment(requestParams.getInt(DeleteKolCommentUseCase.PARAM_ID, 0))
//                        .build()).watcher();
//
//        return RxApollo.from(apolloWatcher).map(kolDeleteCommentMapper);
        return null;
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
