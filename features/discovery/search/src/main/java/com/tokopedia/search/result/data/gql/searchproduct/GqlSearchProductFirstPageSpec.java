package com.tokopedia.search.result.data.gql.searchproduct;

import android.content.Context;

import com.tokopedia.search.R;

import org.jetbrains.annotations.NotNull;

final class GqlSearchProductFirstPageSpec extends GqlSearchProductSpec {

    GqlSearchProductFirstPageSpec(@NotNull Context context) {
        super(context);
    }

    @Override
    public int getResources() {
        return R.raw.gql_search_product_first_page;
    }
}
