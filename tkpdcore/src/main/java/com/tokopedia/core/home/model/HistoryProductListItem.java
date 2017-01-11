package com.tokopedia.core.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.network.entity.home.recentView.RecentView;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * History Product Feed
 */
public class HistoryProductListItem extends RecyclerViewItem implements Parcelable {
    public static final int HISTORY_PRODUCT_LIST_ITEM = 129_212;
    List<RecentView> productItems = new ArrayList<>();

    public HistoryProductListItem() {
        setType(HISTORY_PRODUCT_LIST_ITEM);
    }

    public HistoryProductListItem(List<RecentView> productItems) {
        this();
        this.productItems = productItems;
    }

    public List<RecentView> getProductItems() {
        return productItems;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.productItems);
    }

    protected HistoryProductListItem(Parcel in) {
        super(in);
        this.productItems = in.createTypedArrayList(RecentView.CREATOR);
    }

    public static final Creator<HistoryProductListItem> CREATOR = new Creator<HistoryProductListItem>() {
        @Override
        public HistoryProductListItem createFromParcel(Parcel source) {
            return new HistoryProductListItem(source);
        }

        @Override
        public HistoryProductListItem[] newArray(int size) {
            return new HistoryProductListItem[size];
        }
    };
}
