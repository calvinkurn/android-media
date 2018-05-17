package com.tokopedia.kol.feature.post.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.kol.common.network.GraphqlErrorException;
import com.tokopedia.kol.feature.post.data.pojo.DoLikeKolPost;
import com.tokopedia.kol.feature.post.data.pojo.LikeKolPostResponse;
import com.tokopedia.kol.feature.post.data.pojo.LikeKolPostSuccessData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 17/05/18.
 */

public class LikeKolPostMapper
        implements Func1<Response<GraphqlResponse<LikeKolPostResponse>>, Boolean> {

    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";
    private static final int LIKE_SUCCESS = 1;

    @Inject
    public LikeKolPostMapper() {
    }

    @Override
    public Boolean call(Response<GraphqlResponse<LikeKolPostResponse>> graphqlResponseResponse) {
        LikeKolPostSuccessData data = getDataOrError(graphqlResponseResponse);
        return data.success == LIKE_SUCCESS;
    }

    private LikeKolPostSuccessData getDataOrError(
            Response<GraphqlResponse<LikeKolPostResponse>> graphqlResponseResponse) {
        if (graphqlResponseResponse != null
                && graphqlResponseResponse.body() != null
                && graphqlResponseResponse.body().getData() != null) {
            if (graphqlResponseResponse.isSuccessful()) {
                DoLikeKolPost doLikeKolPost = graphqlResponseResponse.body().getData().doLikeKolPost;
                if (TextUtils.isEmpty(doLikeKolPost.error)) {
                    return doLikeKolPost.data;
                } else {
                    throw new GraphqlErrorException(doLikeKolPost.error);
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }
}
