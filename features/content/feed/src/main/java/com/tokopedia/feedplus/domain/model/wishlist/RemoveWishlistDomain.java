package com.tokopedia.feedplus.domain.model.wishlist;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistDomain {
    private boolean isSuccess;

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
