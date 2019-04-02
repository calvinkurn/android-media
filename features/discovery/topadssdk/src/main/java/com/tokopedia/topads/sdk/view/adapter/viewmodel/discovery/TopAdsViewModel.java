package com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public class TopAdsViewModel implements Item<TopAdsTypeFactory> {

    public static final int TOP_ADS_POSITION_TYPE = -56;
    private final List<Item> list;
    private int position;

    public TopAdsViewModel() {
        list = new ArrayList<>();
        setPosition(TOP_ADS_POSITION_TYPE);
    }

    public TopAdsViewModel(List<Item> list) {
        this.list = list;
        setPosition(TOP_ADS_POSITION_TYPE);
    }

    @Override
    public int type(TopAdsTypeFactory topAdsTypeFactory) {
        return topAdsTypeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Item> getList() {
        return list;
    }

    public void switchDisplayMode(final DisplayMode displayMode) {
        ModelConverter.convertList(list, displayMode);
    }
}
