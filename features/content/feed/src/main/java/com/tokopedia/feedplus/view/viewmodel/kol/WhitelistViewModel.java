package com.tokopedia.feedplus.view.viewmodel.kol;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.WhitelistViewHolder;

/**
 * @author by yfsx on 25/06/18.
 */
public class WhitelistViewModel implements Visitable<FeedPlusTypeFactory> {

    private WhitelistDomain whitelist;

    public WhitelistViewModel(WhitelistDomain whitelist) {
        this.whitelist = whitelist;
    }

    public WhitelistDomain getWhitelist() {
        return whitelist;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return WhitelistViewHolder.LAYOUT;
    }
}
