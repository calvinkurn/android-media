package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.ArrayList;
import java.util.List;

public class CpmDataView implements Visitable<ProductListTypeFactory> {

    private CpmModel cpmModel;

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public CpmDataView() {
    }

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }
}