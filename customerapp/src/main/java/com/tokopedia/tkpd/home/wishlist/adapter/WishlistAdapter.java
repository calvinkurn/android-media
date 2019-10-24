package com.tokopedia.tkpd.home.wishlist.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistAdapterFactory;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptySearchViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptyViewModel;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistAdapter extends BaseAdapter<WishlistAdapterFactory> {

    private WishlistEmptyViewModel emptyViewModel = new WishlistEmptyViewModel();
    private WishlistEmptySearchViewModel emptySearchViewModel = new WishlistEmptySearchViewModel();

    public WishlistAdapter(WishlistAdapterFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public void setEmptyState() {
        this.visitables.clear();
        addElement(emptyViewModel);
    }

    public void setSearchNotFound(String query){
        emptySearchViewModel.setQuery(query);
        this.visitables.clear();
        addElement(emptySearchViewModel);
    }

    public boolean isEmptySearch() {
        return visitables.contains(emptySearchViewModel);
    }

    public boolean isEmptyWishlist() {
        return visitables.contains(emptyViewModel);
    }
}
