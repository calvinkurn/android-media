package com.tokopedia.feedplus.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * Created by meyta on 1/29/18.
 */

public class EmptyFeedBeforeLoginModel implements Visitable<FeedPlusTypeFactory> {

    public EmptyFeedBeforeLoginModel(){}

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}