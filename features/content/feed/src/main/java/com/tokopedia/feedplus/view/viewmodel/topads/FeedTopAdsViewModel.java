package com.tokopedia.feedplus.view.viewmodel.topads;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.List;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopAdsViewModel implements Visitable<FeedPlusTypeFactory> {

    private final List<Data> list;

    public FeedTopAdsViewModel(List<Data> list) {
        this.list = list;
    }

    public List<Data> getList() {
        return list;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
