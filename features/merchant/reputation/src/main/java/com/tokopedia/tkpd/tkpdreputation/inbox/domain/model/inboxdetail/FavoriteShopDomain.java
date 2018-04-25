package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 9/26/17.
 */

public class FavoriteShopDomain {
    private final int isSuccess;

    public FavoriteShopDomain(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getIsSuccess() {
        return isSuccess;
    }
}
