package com.tokopedia.posapp.domain.model.result;

/**
 * Created by okasurya on 8/31/17.
 */

public class ProductSavedResult {
    private boolean status;
    private String nextUri;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }
}
