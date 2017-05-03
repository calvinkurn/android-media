package com.tokopedia.tkpd.home.feed.view.viewModel;

import com.tokopedia.core.var.RecyclerViewItem;

/**
 * @author Kulomady on 2/27/17.
 */

public class EmptyFeedModel extends RecyclerViewItem {
    public static final int EMPTY_FEED= 88888;

    public EmptyFeedModel() {
        super.setType(EMPTY_FEED);
    }
}
