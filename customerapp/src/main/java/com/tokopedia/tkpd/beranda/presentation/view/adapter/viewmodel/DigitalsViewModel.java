package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewModel implements Visitable<HomeTypeFactory> {

    private String title;

    public DigitalsViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
