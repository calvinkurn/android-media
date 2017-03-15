package com.tokopedia.tkpd.home.wishlist.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 2/20/17.
 */

public class DataWishlist {
    private boolean isDataValid;
    private List<WishlistDomain> Wishlists = new ArrayList<>();
     private String nextUrl;

    public List<WishlistDomain> getWishlists() {
        return Wishlists;
    }

    public void setWishlists(List<WishlistDomain> wishlists) {
        Wishlists = wishlists;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
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
