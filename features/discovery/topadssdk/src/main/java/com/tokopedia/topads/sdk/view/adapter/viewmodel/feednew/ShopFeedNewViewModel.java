package com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.adapter.factory.FeedTypeFactory;

/**
 * @author by milhamj on 29/03/18.
 */

public class ShopFeedNewViewModel implements Item<FeedTypeFactory> {

    private Data data;

    @Override
    public int type(FeedTypeFactory typeFactory) {
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
}
