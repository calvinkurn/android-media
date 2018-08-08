package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.List;

/**
 * Created by errysuprayogi on 2/20/18.
 */

public class TopAdsViewModel implements Visitable<HomeTypeFactory> {

    private List<Data> dataList;

    public TopAdsViewModel(List<Data> dataList) {
        this.dataList = dataList;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
