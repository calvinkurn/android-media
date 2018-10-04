package com.tokopedia.feedplus.view.viewmodel.kol;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.ProductCommunicationViewHolder;

import java.util.List;

/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationViewModel implements Visitable<FeedPlusTypeFactory> {

    private List<ProductCommunicationItemViewModel> itemViewModels;

    public ProductCommunicationViewModel(List<ProductCommunicationItemViewModel> itemViewModels) {
        this.itemViewModels = itemViewModels;
    }

    public List<ProductCommunicationItemViewModel> getItemViewModels() {
        return itemViewModels;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return ProductCommunicationViewHolder.LAYOUT;
    }
    
}
