package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;

public class TopAdsViewModel implements Visitable<ProductListTypeFactory> {


    private Data topadsData = new Data();
    private String query;

    public TopAdsViewModel(Data data, String query) {
        this.topadsData = data;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Data getTopadsData() {
        return topadsData;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
