package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class UseCaseIconSectionViewModel implements Visitable<HomeTypeFactory> {

    private List<HomeIconItem> itemList;

    public UseCaseIconSectionViewModel() {
        itemList = new ArrayList<>();
    }

    public List<HomeIconItem> getItemList() {
        return itemList;
    }

    public void addItem(HomeIconItem item){
        this.itemList.add(item);
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
