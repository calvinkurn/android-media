package com.tokopedia.tkpd.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public class ProductCardViewModel implements Visitable<FeedPlusTypeFactory>, Parcelable {

    private String shopName;
    private String shopAvatar;
    private ArrayList<ProductFeedViewModel> listProduct;
    private String actionText;
    private boolean isGoldMerchant;
    private String postTime;

    public ProductCardViewModel() {
    }

    public ProductCardViewModel(String s, ArrayList<ProductFeedViewModel> listProduct) {
        this.shopName = s;
        this.listProduct = listProduct;
        this.actionText = "ubah 1 produk";
        this.shopAvatar = "https://imagerouter.tokopedia.com/img/100-square/shops-1/2016/8/5/1205649/1205649_620e3ec4-9a94-4210-bac4-f31ab1d1b9f5.jpg";
        this.isGoldMerchant = true;
        this.postTime = "2017-05-17T15:10:53+07:00";
    }

    protected ProductCardViewModel(Parcel in) {
        shopName = in.readString();
        shopAvatar = in.readString();
        listProduct = in.createTypedArrayList(ProductFeedViewModel.CREATOR);
        actionText = in.readString();
        isGoldMerchant = in.readByte() != 0;
        postTime = in.readString();
    }

    public static final Creator<ProductCardViewModel> CREATOR = new Creator<ProductCardViewModel>() {
        @Override
        public ProductCardViewModel createFromParcel(Parcel in) {
            return new ProductCardViewModel(in);
        }

        @Override
        public ProductCardViewModel[] newArray(int size) {
            return new ProductCardViewModel[size];
        }
    };

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }


    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeString(shopAvatar);
        dest.writeTypedList(listProduct);
        dest.writeString(actionText);
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeString(postTime);
    }
}
