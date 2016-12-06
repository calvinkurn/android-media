package com.tokopedia.core.home.model;

import android.text.Html;

import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 27/10/2015.
 */
public class HorizontalProductList extends RecyclerViewItem{
    List<ProductItem> listProduct;

    public HorizontalProductList(){
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public HorizontalProductList(List<? extends RecyclerViewItem> product) {
        listProduct = new ArrayList<>();
        if (product != null){
            for(int i=0;i<product.size();i++){
                if(product.get(i) instanceof ProductItem){
                    ProductItem productItem = (ProductItem) product.get(i);
                    productItem.setShop(Html.fromHtml(productItem.getShop()).toString());
                    listProduct.add(productItem);
                }
            }
        }
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public List<ProductItem> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductItem> listProduct) {
        this.listProduct = listProduct;
    }

    public void addAll(List<ProductItem> items){
        this.listProduct.addAll(items);
    }

    public void clear(){
        this.listProduct.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HorizontalProductList that = (HorizontalProductList) o;

        return !(listProduct != null ? !listProduct.equals(that.listProduct) : that.listProduct != null);

    }

    @Override
    public int hashCode() {
        return listProduct != null ? listProduct.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HorizontalProductList{" +
                "mRecentViewList=" + listProduct +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.listProduct);
    }

    protected HorizontalProductList(android.os.Parcel in) {
        super(in);
        this.listProduct = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public static final Creator<HorizontalProductList> CREATOR = new Creator<HorizontalProductList>() {
        @Override
        public HorizontalProductList createFromParcel(android.os.Parcel source) {
            return new HorizontalProductList(source);
        }

        @Override
        public HorizontalProductList[] newArray(int size) {
            return new HorizontalProductList[size];
        }
    };
}
