package com.tokopedia.topads.sdk.view.adapter.viewmodel.feed;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.AdsTypeFactory;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class ShopFeedViewModel implements Item<AdsTypeFactory> {

    Data data;
    private DisplayMode displayMode;

    @Override
    public int type(AdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int originalPos() {
        return 0;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }
}
