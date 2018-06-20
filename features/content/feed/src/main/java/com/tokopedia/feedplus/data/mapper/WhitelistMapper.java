package com.tokopedia.feedplus.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.feedplus.data.pojo.Whitelist;
import com.tokopedia.feedplus.data.pojo.WhitelistContent;
import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.domain.model.feed.WhitelistContentDomain;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 20/06/18.
 */
public class WhitelistMapper implements Func1<Response<GraphqlResponse<WhitelistQuery>>, WhitelistDomain> {

    private static final String ERROR_SERVER = "ERROR_SERVER";
    private static final String ERROR_NETWORK = "ERROR_NETWORK";
    private static final String ERROR_EMPTY_RESPONSE = "ERROR_EMPTY_RESPONSE";

    @Override
    public WhitelistDomain call(Response<GraphqlResponse<WhitelistQuery>> response) {
        return mappingToDomain(getDataOrError(response));
    }

    @Inject
    public WhitelistMapper() {

    }

    private WhitelistDomain mappingToDomain(Whitelist response) {
        WhitelistDomain domain = new WhitelistDomain();
        domain.setError(response.getError());
        domain.setContent(mappingContentDomain(response.getContent()));
        return domain;
    }

    private Whitelist getDataOrError(Response<GraphqlResponse<WhitelistQuery>> response) {
        if (response != null
                && response.body() != null
                && response.body().getData() != null) {
            if (response.isSuccessful()) {
                WhitelistQuery whitelistQuery = response.body().getData();
                if (whitelistQuery.getWhitelist() != null) {
                    return whitelistQuery.getWhitelist();
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

    private WhitelistContentDomain mappingContentDomain(WhitelistContent response) {
        WhitelistContentDomain domain = new WhitelistContentDomain();
        domain.setUrl(response.getUrl());
        domain.setWhitelist(response.isWhitelist());
        return domain;
    }
}
