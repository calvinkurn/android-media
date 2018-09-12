package com.tokopedia.kolcommon.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kolcommon.R;
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;

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
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_whitelist),
                WhitelistQuery.class
        );
    }
}
