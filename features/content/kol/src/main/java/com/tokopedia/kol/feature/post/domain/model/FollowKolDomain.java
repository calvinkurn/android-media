package com.tokopedia.kol.feature.post.domain.model;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolDomain {
    private final int status;

    public FollowKolDomain(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
