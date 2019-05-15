package com.tokopedia.search.result.data.gql.dynamicfilter;

import android.content.Context;

import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.search.R;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

final class GqlDynamicFilterSpec extends GqlSpecification {

    GqlDynamicFilterSpec(@NotNull Context context) {
        super(context);
    }

    @Override
    public int getResources() {
        return R.raw.gql_search_filter_product;
    }

    @NotNull
    @Override
    public Type getType() {
        return GqlDynamicFilterResponse.class;
    }
}
