package com.tokopedia.affiliate.common.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

/**
 * @author by yfsx on 12/10/18.
 */
public class CheckQuotaUseCase extends GraphqlUseCase {

    private final Context context;

    @Inject
    public CheckQuotaUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest() {
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_af_quota),
                CheckQuotaQuery.class, false
        );
    }
}
