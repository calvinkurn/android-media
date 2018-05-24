package com.tokopedia.feedplus.view.viewmodel.product;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public abstract class ProductCardViewModel implements Visitable<FeedPlusTypeFactory> {

    protected ArrayList<ProductFeedViewModel> listProduct;

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }
}
