package com.tokopedia.feedplus.view.viewmodel.kol;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by milhamj on 14/05/18.
 */

public class PollViewModel implements Visitable<FeedPlusTypeFactory> {
    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
