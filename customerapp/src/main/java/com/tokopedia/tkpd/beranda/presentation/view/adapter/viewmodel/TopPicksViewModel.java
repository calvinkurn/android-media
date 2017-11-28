package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    private TopPicksModel topPicksModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TopPicksModel getTopPicksModel() {
        return topPicksModel;
    }

    public void setTopPicksModel(TopPicksModel topPicksModel) {
        this.topPicksModel = topPicksModel;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
