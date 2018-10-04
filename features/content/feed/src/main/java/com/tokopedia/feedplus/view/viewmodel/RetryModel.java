package com.tokopedia.feedplus.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by milhamj on 02/05/18.
 */

public class RetryModel implements Visitable<FeedPlusTypeFactory> {

    @Override
    public int type(FeedPlusTypeFactory feedPlusTypeFactory) {
        return feedPlusTypeFactory.type(this);
    }

}
