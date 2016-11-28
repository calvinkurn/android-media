package com.tokopedia.sellerapp.home.model.wishlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class WishlistData {

    @SerializedName("data")
    List<Wishlist> Wishlist = new ArrayList<>();
    @SerializedName("pagination")
    Pagination Paging;

    public List<com.tokopedia.tkpd.home.model.wishlist.Wishlist> getWishlist() {
        return Wishlist;
    }

    public void setWishlist(List<com.tokopedia.tkpd.home.model.wishlist.Wishlist> wishlist) {
        Wishlist = wishlist;
    }

    public Pagination getPaging() {
        return Paging;
    }

    public void setPaging(Pagination paging) {
        Paging = paging;
    }
}
