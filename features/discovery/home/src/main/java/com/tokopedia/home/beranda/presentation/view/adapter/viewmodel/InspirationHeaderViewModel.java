package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

public class InspirationHeaderViewModel implements Visitable<HomeTypeFactory> {

    private String title;

    public InspirationHeaderViewModel(String title) {
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
