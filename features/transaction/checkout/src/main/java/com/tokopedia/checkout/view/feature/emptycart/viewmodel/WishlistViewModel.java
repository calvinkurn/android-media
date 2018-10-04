package com.tokopedia.checkout.view.feature.emptycart.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class WishlistViewModel implements Parcelable {

    private Wishlist wishlist;

    public WishlistViewModel() {
    }

    protected WishlistViewModel(Parcel in) {
        wishlist = in.readParcelable(Wishlist.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(wishlist, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WishlistViewModel> CREATOR = new Creator<WishlistViewModel>() {
        @Override
        public WishlistViewModel createFromParcel(Parcel in) {
            return new WishlistViewModel(in);
        }

        @Override
        public WishlistViewModel[] newArray(int size) {
            return new WishlistViewModel[size];
        }
    };

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }
}
