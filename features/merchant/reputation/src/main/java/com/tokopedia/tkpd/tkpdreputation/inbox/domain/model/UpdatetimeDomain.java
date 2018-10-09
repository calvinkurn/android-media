package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class UpdatetimeDomain {

    private String time;
    private boolean valid;

    public UpdatetimeDomain(String time, boolean valid) {
        this.time = time;
        this.valid = valid;
    }
}
