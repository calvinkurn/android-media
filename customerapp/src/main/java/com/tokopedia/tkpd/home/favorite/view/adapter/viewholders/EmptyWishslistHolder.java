package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.EmptyWishlistViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class EmptyWishslistHolder extends AbstractViewHolder<EmptyWishlistViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.empty_wishlist;

    public EmptyWishslistHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void bind(EmptyWishlistViewModel emptyWishlistViewModel) {

    }

}
