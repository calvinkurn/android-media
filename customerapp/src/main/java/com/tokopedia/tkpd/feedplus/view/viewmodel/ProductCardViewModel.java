package com.tokopedia.tkpd.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusTypeFactory;

/**
 * @author by nisie on 5/15/17.
 */

public class ProductCardViewModel implements Visitable<FeedPlusTypeFactory> {

    String authorName;

    public ProductCardViewModel(String s) {
        this.authorName = s;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
