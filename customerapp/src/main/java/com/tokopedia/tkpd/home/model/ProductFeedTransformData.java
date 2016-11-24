package com.tokopedia.tkpd.home.model;

import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.network.entity.home.GetListFaveShopId;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ProductItem;

import java.util.List;
import java.util.Map;

/**
 * Created by m.normansyah on 27/11/2015.
 */
public class ProductFeedTransformData {
    Map<String, String> header;
    Map<String, String> content;

    // this is the data
    String shopId;
    HorizontalProductList horizontalProductList;
    List<ProductItem> listProductItems;
    PagingHandler.PagingHandlerModel pagingHandlerModel;
    GetListFaveShopId getListFaveShopId;

    public ProductFeedTransformData(){}

    public ProductFeedTransformData(Map<String, String> header,
                                    Map<String, String> content,
                                    HorizontalProductList horizontalProductList) {

        this.header = header;
        this.content = content;
        this.horizontalProductList = horizontalProductList;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }

    public HorizontalProductList getHorizontalProductList() {
        return horizontalProductList;
    }

    public void setHorizontalProductList(HorizontalProductList horizontalProductList) {
        this.horizontalProductList = horizontalProductList;
    }

    public List<ProductItem> getListProductItems() {
        return listProductItems;
    }

    public void setListProductItems(List<ProductItem> listProductItems) {
        this.listProductItems = listProductItems;
    }

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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public GetListFaveShopId getGetListFaveShopId() {
        return getListFaveShopId;
    }

    public void setGetListFaveShopId(GetListFaveShopId getListFaveShopId) {
        this.getListFaveShopId = getListFaveShopId;
    }

    @Override
    public String toString() {
        /*return "Pair{" +
                "header=" + header +
                ", content=" + content +
                '}';*/
        return super.toString();
    }
}
