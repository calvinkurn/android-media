package com.tokopedia.tkpd.home.favorite.domain.model;

import com.tokopedia.core.database.model.PagingHandler;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

public class FavoriteShop {

    private boolean mIsDataValid;
    private String mMessage;
    private List<FavoriteShopItem> mData;
    private PagingHandler.PagingHandlerModel pagingModel;

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

    public PagingHandler.PagingHandlerModel getPagingModel() {
        return pagingModel;
    }

    public void setPagingModel(PagingHandler.PagingHandlerModel pagingModel) {
        this.pagingModel = pagingModel;
    }
}
