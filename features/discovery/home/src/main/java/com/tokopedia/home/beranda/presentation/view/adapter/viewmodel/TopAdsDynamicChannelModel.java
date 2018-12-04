package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.topads.sdk.base.adapter.Item;

import java.util.List;

/**
 * Author errysuprayogi on 30,November,2018
 */
public class TopAdsDynamicChannelModel implements Visitable<HomeTypeFactory> {
    private String title;
    private List<Item> items;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
