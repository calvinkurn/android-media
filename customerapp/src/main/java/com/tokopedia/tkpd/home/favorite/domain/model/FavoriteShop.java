package com.tokopedia.tkpd.home.favorite.domain.model;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

public class FavoriteShop {

    private boolean mIsDataValid;
    private String mMessage;
    private List<FavoriteShopItem> mData;

    public void setDataIsValid(boolean isDataValid) {

        mIsDataValid = isDataValid;
    }

    public void setMessage(String message) {

        mMessage = message;
    }

    public boolean isDataValid() {
        return mIsDataValid;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setDataValid(boolean dataValid) {
        mIsDataValid = dataValid;
    }

    public List<FavoriteShopItem> getData() {
        return mData;
    }

    public void setData(List<FavoriteShopItem> data) {
        mData = data;
    }
}
