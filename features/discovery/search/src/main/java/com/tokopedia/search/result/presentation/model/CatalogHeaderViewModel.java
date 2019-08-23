package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.CatalogListTypeFactory;

public class CatalogHeaderViewModel implements Visitable<CatalogListTypeFactory> {

    @Override
    public int type(CatalogListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public CatalogHeaderViewModel() {
    }
}
