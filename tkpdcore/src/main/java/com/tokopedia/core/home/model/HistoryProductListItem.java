package com.tokopedia.core.home.model;

import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcel;

import java.util.List;

/**
 * History Product Feed
 */
@Parcel
public class HistoryProductListItem extends RecyclerViewItem {
    public static final int HISTORY_PRODUCT_LIST_ITEM = 129_212;
    List<ProductItem> productItems;

    public HistoryProductListItem() {
        setType(HISTORY_PRODUCT_LIST_ITEM);
    }

    public HistoryProductListItem(List<ProductItem> productItems) {
        this();
        this.productItems = productItems;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }
}
