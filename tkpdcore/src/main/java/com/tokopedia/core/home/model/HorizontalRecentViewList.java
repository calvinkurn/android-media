package com.tokopedia.core.home.model;

import android.os.Parcelable;
import android.text.Html;

import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

public class HorizontalRecentViewList extends RecyclerViewItem implements Parcelable {
    List<ProductItem> mRecentViewList;

    public HorizontalRecentViewList() {
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public HorizontalRecentViewList(List<? extends RecyclerViewItem> product) {
        mRecentViewList = new ArrayList<>();
        if (product != null) {
            for (int i = 0; i < product.size(); i++) {
                if (product.get(i) instanceof ProductItem) {
                    ProductItem productItem = (ProductItem) product.get(i);
                    productItem.setShop(Html.fromHtml(productItem.getShop()).toString());
                    mRecentViewList.add(productItem);
                }
            }
        }
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public List<ProductItem> getRecentViewList() {
        return mRecentViewList;
    }

    public void setRecentViewList(List<ProductItem> recentViewList) {
        this.mRecentViewList = recentViewList;
    }

    public void addAll(List<ProductItem> items) {
        this.mRecentViewList.addAll(items);
    }

    public void clear() {
        this.mRecentViewList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HorizontalRecentViewList that = (HorizontalRecentViewList) o;

        return !(mRecentViewList != null
                ? !mRecentViewList.equals(that.mRecentViewList)
                : that.mRecentViewList != null);

    }

    @Override
    public int hashCode() {
        return mRecentViewList != null ? mRecentViewList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HorizontalProductList{" +
                "mRecentViewList=" + mRecentViewList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.mRecentViewList);
    }

    protected HorizontalRecentViewList(android.os.Parcel in) {
        super(in);
        this.mRecentViewList = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public static final Creator<HorizontalRecentViewList> CREATOR
            = new Creator<HorizontalRecentViewList>() {
        @Override
        public HorizontalRecentViewList createFromParcel(android.os.Parcel source) {
            return new HorizontalRecentViewList(source);
        }

        @Override
        public HorizontalRecentViewList[] newArray(int size) {
            return new HorizontalRecentViewList[size];
        }
    };
}
