package com.tokopedia.core.analytics.model;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nakama on 11/15/17.
 */

public class Hotlist {
    private String actionName;
    private String screenName;
    private String hotlistAlias;
    private int position;
    private List<Product> productList;

    public String getActionName() {
        return actionName;
    }

    public void setScreenName(String label) {
        this.screenName = label;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setHotlistAlias(String hotlistAlias) {
        this.hotlistAlias = hotlistAlias;
    }

    public String getHotlistAlias() {
        return hotlistAlias;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<Object> getProduct() {
        List<Object> objects = new ArrayList<>();
        for (Product product : productList) {
            objects.add(product.getProduct());
        }
        return objects;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public static class Product {
        private String productName;
        private String productID;
        private String categoryName;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public Map<String, Object> getProduct() {
            return DataLayer.mapOf(
                    "name", getProductName(),
                    "id", getProductID(),
                    "category", getCategoryName()
            );
        }
    }
}
