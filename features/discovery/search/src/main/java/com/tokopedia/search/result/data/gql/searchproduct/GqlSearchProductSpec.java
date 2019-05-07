package com.tokopedia.search.result.data.gql.searchproduct;

import android.content.Context;

import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.search.result.domain.model.SearchProductModel;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

abstract class GqlSearchProductSpec extends GqlSpecification {

    public GqlSearchProductSpec(@NotNull Context context) {
        super(context);
    }

    @NotNull
    @Override
    public Type getType() {
        return SearchProductModel.class;
    }
}
