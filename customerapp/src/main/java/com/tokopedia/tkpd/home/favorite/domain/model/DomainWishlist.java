package com.tokopedia.tkpd.home.favorite.domain.model;

import java.util.List;

/**
 * @author Kulomady on 1/17/17.
 */

public class DomainWishlist {

    private boolean mIsValid;
    private String mMessage;
    private List<DataWishlist> mData;

    public void setDataIsValid(boolean isValid) {
        mIsValid = isValid;
    }

    public boolean isValid() {
        return mIsValid;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setValid(boolean valid) {
        mIsValid = valid;
    }

    public List<DataWishlist> getData() {
        return mData;
    }

    public void setData(List<DataWishlist> data) {
        mData = data;
    }
}
