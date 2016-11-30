package com.tokopedia.tkpd.home.model;

import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ShopItem;

import java.util.List;
import java.util.Map;

/**
 * Created by m.normansyah on 27/11/2015.
 */
public class FavoriteTransformData {
    Map<String, String> header;
    Map<String, String> content;

    HorizontalProductList horizontalProductList;
    HorizontalShopList horizontalShopList;
    List<ShopItem> shopItems;
    PagingHandler.PagingHandlerModel pagingHandlerModel;

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public HorizontalProductList getHorizontalProductList() {
        return horizontalProductList;
    }

    public void setHorizontalProductList(HorizontalProductList horizontalProductList) {
        this.horizontalProductList = horizontalProductList;
    }

    public HorizontalShopList getHorizontalShopList() {
        return horizontalShopList;
    }

    public void setHorizontalShopList(HorizontalShopList horizontalShopList) {
        this.horizontalShopList = horizontalShopList;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }
}
