package com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsTypeFactory;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public class LoadingViewModel implements Item<TopAdsTypeFactory> {

    int position;

    public static int LOADING_POSITION_TYPE = -101010;

    public LoadingViewModel() {
        setPosition(LOADING_POSITION_TYPE);
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
}
