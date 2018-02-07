package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class SellViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    private String subtitle;
    private String btn_title;

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBtn_title() {
        return btn_title;
    }

    public void setBtn_title(String btn_title) {
        this.btn_title = btn_title;
    }
}
