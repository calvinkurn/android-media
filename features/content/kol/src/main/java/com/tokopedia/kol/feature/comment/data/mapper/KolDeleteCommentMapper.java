package com.tokopedia.kol.feature.comment.data.mapper;

import android.text.TextUtils;

import com.tokopedia.kol.feature.comment.data.pojo.delete.DeleteCommentKolData;
import com.tokopedia.kol.feature.comment.data.pojo.delete.DeleteCommentKolGraphql;
import com.tokopedia.kolcommon.util.GraphqlErrorException;
import com.tokopedia.network.data.model.response.GraphqlResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/10/17.
 */

public class KolDeleteCommentMapper
        implements Func1<Response<GraphqlResponse<DeleteCommentKolGraphql>>, Boolean> {

    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Inject
    KolDeleteCommentMapper() {
    }

    @Override
    public Boolean call(Response<GraphqlResponse<DeleteCommentKolGraphql>>
                                graphqlResponseResponse) {
        return isDeleteSuccesful(getDataorError(graphqlResponseResponse));
    }

    private DeleteCommentKolData getDataorError(
            Response<GraphqlResponse<DeleteCommentKolGraphql>> deleteCommentKolResponse) {
        if (deleteCommentKolResponse != null
                && deleteCommentKolResponse.body() != null
                && deleteCommentKolResponse.body().getData() != null) {
            if (deleteCommentKolResponse.isSuccessful()) {
                DeleteCommentKolGraphql data = deleteCommentKolResponse.body().getData();
                if (TextUtils.isEmpty(data.getDeleteCommentKol().getError())) {
                    return data.getDeleteCommentKol().getData();
                } else {
                    throw new GraphqlErrorException(data.getDeleteCommentKol().getError());
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }

    private Boolean isDeleteSuccesful(DeleteCommentKolData data) {
        return (data != null && data.getSuccess() == 1);
    }
}