package com.tokopedia.core.home.model;

import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * History Product Feed
 */
public class HistoryProductListItem extends RecyclerViewItem {
    public static final int HISTORY_PRODUCT_LIST_ITEM = 129_212;
    List<ProductItem> productItems = new ArrayList<>();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.productItems);
    }

    protected HistoryProductListItem(android.os.Parcel in) {
        super(in);
        this.productItems = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public static final Creator<HistoryProductListItem> CREATOR = new Creator<HistoryProductListItem>() {
        @Override
        public HistoryProductListItem createFromParcel(android.os.Parcel source) {
            return new HistoryProductListItem(source);
        }

        @Override
        public HistoryProductListItem[] newArray(int size) {
            return new HistoryProductListItem[size];
        }
    };
}
