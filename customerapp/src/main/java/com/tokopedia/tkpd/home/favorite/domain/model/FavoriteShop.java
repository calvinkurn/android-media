package com.tokopedia.tkpd.home.favorite.domain.model;

import com.tokopedia.core.database.model.PagingHandler;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

public class FavoriteShop {

    private boolean isDataValid;
    private String message;
    private List<FavoriteShopItem> data;
    private boolean isNetworkError;
    private PagingHandler.PagingHandlerModel pagingModel;

    public void setDataIsValid(boolean isDataValid) {

        this.isDataValid = isDataValid;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public String getMessage() {
        return message;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public List<FavoriteShopItem> getData() {
        return data;
    }

    public void setData(List<FavoriteShopItem> data) {
        this.data = data;
    }

    public PagingHandler.PagingHandlerModel getPagingModel() {
        return pagingModel;
    }

    public void setPagingModel(PagingHandler.PagingHandlerModel pagingModel) {
        this.pagingModel = pagingModel;
    }

    public boolean isNetworkError() {
        return isNetworkError;
    }

    public void setNetworkError(boolean networkError) {
        isNetworkError = networkError;
    }
}
