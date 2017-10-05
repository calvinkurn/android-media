package com.tokopedia.tkpd.home.wishlist.domain.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.wishlist.Pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 2/20/17.
 */

public class DataWishlist {
    private boolean isDataValid;
    private List<WishlistDomain> Wishlists = new ArrayList<>();
    @SerializedName("pagination")
    Pagination Paging;

    public List<WishlistDomain> getWishlists() {
        return Wishlists;
    }

    public void setWishlists(List<WishlistDomain> wishlists) {
        Wishlists = wishlists;
    }

    public Pagination getPaging() {
        return Paging;
    }

    public void setPaging(Pagination paging) {
        Paging = paging;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public void setDataInValid() {
        isDataValid = false;
    }

    public void setDataValid() {
        isDataValid = true;
    }
}
