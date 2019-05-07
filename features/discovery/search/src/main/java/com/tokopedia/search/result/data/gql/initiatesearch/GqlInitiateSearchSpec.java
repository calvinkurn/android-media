package com.tokopedia.search.result.data.gql.initiatesearch;

import android.content.Context;

import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.R;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

final class GqlInitiateSearchSpec extends GqlSpecification {

    GqlInitiateSearchSpec(@NotNull Context context) {
        super(context);
    }

    @Override
    public int getResources() {
        return R.raw.gql_initiate_search;
    }

    @NotNull
    @Override
    public Type getType() {
        return InitiateSearchModel.class;
    }
}
