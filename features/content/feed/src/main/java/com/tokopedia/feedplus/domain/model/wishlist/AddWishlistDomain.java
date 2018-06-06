package com.tokopedia.feedplus.domain.model.wishlist;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistDomain {
    private boolean isSuccess;

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
