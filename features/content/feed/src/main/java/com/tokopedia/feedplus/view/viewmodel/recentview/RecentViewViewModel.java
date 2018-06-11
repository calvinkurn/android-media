package com.tokopedia.feedplus.view.viewmodel.recentview;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewViewModel implements Visitable<FeedPlusTypeFactory> {

    private final ArrayList<RecentViewProductViewModel> listProduct;

    public RecentViewViewModel(ArrayList<RecentViewProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public ArrayList<RecentViewProductViewModel> getListProduct() {
        return listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
