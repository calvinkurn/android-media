package com.tokopedia.search.result.gql.initiatesearch;

import android.content.Context;

import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.search.R;

import org.jetbrains.annotations.NotNull;

final class GqlInitiateSearchSpec extends GqlSpecification {

    GqlInitiateSearchSpec(@NotNull Context context) {
        super(context);
    }

    @Override
    public int getResources() {
        return R.raw.gql_initiate_search;
    }
}
