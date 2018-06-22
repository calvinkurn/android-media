package com.tokopedia.feedplus.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistUseCase extends GraphqlUseCase {

    private final Context context;

    @Inject
    public GetWhitelistUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest() {
        return new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),R.raw.query_whitelist), WhitelistQuery.class);
    }
}
