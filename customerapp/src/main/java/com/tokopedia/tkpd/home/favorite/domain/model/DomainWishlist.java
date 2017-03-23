package com.tokopedia.tkpd.home.favorite.domain.model;

import java.util.List;

/**
 * @author Kulomady on 1/17/17.
 */

public class DomainWishlist {

    private boolean isValid;
    private String message;
    private List<DataWishlist> dataWishlists;

    public void setDataIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<DataWishlist> getData() {
        return dataWishlists;
    }

    public void setData(List<DataWishlist> data) {
        dataWishlists = data;
    }
}
