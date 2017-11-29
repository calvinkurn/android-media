package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class TopPicksViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    private List<TopPicksItemModel> topPicksItems;
    private String topPickUrl;

    public String getTopPickUrl() {
        return topPickUrl;
    }

    public void setTopPickUrl(String topPickUrl) {
        this.topPickUrl = topPickUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TopPicksItemModel> getTopPicksItems() {
        return topPicksItems;
    }

    public void setTopPicksItems(List<TopPicksItemModel> topPicksItems) {
        this.topPicksItems = topPicksItems;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
