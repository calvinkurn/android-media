package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

public class InspirationHeaderDataModel implements Visitable<HomeTypeFactory> {

    private String title;

    public InspirationHeaderDataModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
