package com.tokopedia.feedplus.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;

import com.tokopedia.feedplus.data.pojo.FollowKol;
import com.tokopedia.feedplus.data.pojo.FollowKolQuery;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolMapper implements Func1<Response<GraphqlResponse<FollowKolQuery>>, FollowKolDomain> {

    private static final String ERROR_SERVER = "ERROR_SERVER";
    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Override
    public FollowKolDomain call(Response<GraphqlResponse<FollowKolQuery>> response) {
        FollowKol followKol = getDataOrError(response);
        return convertToDomain(followKol);
    }

    @Inject
    public FollowKolMapper() {

    }

    private FollowKol getDataOrError(Response<GraphqlResponse<FollowKolQuery>> response) {
        if (response != null
                && response.body() != null
                && response.body().getData() != null) {
            if (response.isSuccessful()) {
                FollowKolQuery query = response.body().getData();
                if (query.getData() != null) {
                    return query.getData();
                } else {
                    throw new RuntimeException(ERROR_SERVER);
                }
            } else {
                throw new RuntimeException(ERROR_NETWORK);
            }
        } else {
            throw new RuntimeException(ERROR_EMPTY_RESPONSE);
        }
    }


    private FollowKolDomain convertToDomain(FollowKol data) {
        return new FollowKolDomain(data.getData().getStatus());
    }
}
